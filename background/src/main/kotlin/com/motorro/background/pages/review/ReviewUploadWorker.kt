package com.motorro.background.pages.review

import android.content.Context
import androidx.core.net.toUri
import androidx.hilt.work.HiltWorker
import androidx.work.ArrayCreatingInputMerger
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkRequest
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.motorro.background.pages.review.data.Photo
import com.motorro.background.pages.review.data.ReviewData
import com.motorro.background.pages.review.net.ReviewApi
import com.motorro.core.log.Logging
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.Duration

@HiltWorker
class ReviewUploadWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val reviewApi: ReviewApi
) : CoroutineWorker(appContext, params), Logging {

    companion object {
        /**
         * Schedules photo upload
         */
        fun createJob(rating: Int, review: String) = OneTimeWorkRequestBuilder<ReviewUploadWorker>()
            .addTag(ReviewConstants.TAG)
            .setInputData(
                workDataOf(
                    ReviewConstants.KEY_RATING to rating,
                    ReviewConstants.KEY_REVIEW_TEXT to review
                )
            )
            .setInputMerger(ArrayCreatingInputMerger::class.java)
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
        val rating = checkNotNull(inputData.getIntArray(ReviewConstants.KEY_RATING)?.firstOrNull())
        val text = checkNotNull(inputData.getStringArray(ReviewConstants.KEY_REVIEW_TEXT)?.firstOrNull())
        val photos = inputData.getStringArray(ReviewConstants.KEY_PHOTO).orEmpty().map { Photo(it.toUri()) }

        val data = ReviewData(
            rating = rating,
            text = text,
            photos = photos
        )

        i { "Uploading review: $data" }

        return try {
            reviewApi.uploadReview(data)
            Result.success()
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