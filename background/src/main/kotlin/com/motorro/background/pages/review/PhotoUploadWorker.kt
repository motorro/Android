package com.motorro.background.pages.review

import android.content.Context
import androidx.core.net.toUri
import androidx.hilt.work.HiltWorker
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkRequest
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.motorro.background.pages.review.data.Photo
import com.motorro.background.pages.review.net.ReviewApi
import com.motorro.core.log.Logging
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.Duration

@HiltWorker
class PhotoUploadWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val reviewApi: ReviewApi
) : CoroutineWorker(appContext, params), Logging {

    companion object {
        /**
         * Schedules photo upload
         */
        fun createJob(photo: Photo) = OneTimeWorkRequestBuilder<PhotoUploadWorker>()
            .addTag(ReviewConstants.TAG)
            .setInputData(workDataOf(ReviewConstants.KEY_PHOTO to photo.uri.toString()))
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, Duration.ofMillis(WorkRequest.MIN_BACKOFF_MILLIS))
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .setRequiresBatteryNotLow(true)
                    .setRequiresStorageNotLow(true)
                    .build()
            )
            .build()
    }

    override suspend fun doWork(): Result {
        val photo = Photo(checkNotNull(inputData.getString(ReviewConstants.KEY_PHOTO)).toUri())
        i { "Uploading photo: ${photo.uri}" }

        if (runAttemptCount > ReviewConstants.MAX_RETRY_COUNT) {
            w { "Upload retry attempts exhausted. Failure..." }
            return Result.failure()
        }

        return try {
            val uri = reviewApi.uploadPhoto(photo)
            Result.success(workDataOf(ReviewConstants.KEY_PHOTO to uri.toString()))
        } catch (e: Throwable) {
            w(e) { "Worker failed" }
            if (runAttemptCount >= ReviewConstants.MAX_RETRY_COUNT) {
                w { "Upload retry attempts exhausted. Failure..." }
                Result.failure()
            } else {
                d { "Retrying..." }
                Result.retry()
            }
        }
    }
}