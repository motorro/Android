package com.motorro.notifications.fcm

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import javax.inject.Inject

@HiltWorker
class UploadTokenWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val pushTokenService: PushTokenService
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val token = inputData.getString(KEY_TOKEN)?.let(::FcmToken) ?: return Result.failure()
        return try {
            pushTokenService.register(token)
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    /**
     * [UploadTokenWorker] scheduler
     */
    interface Scheduler {
        /**
         * Schedules a task to upload a push token
         * @param token Token to upload
         */
        operator fun invoke(token: FcmToken)

        /**
         * Default implementation of [Scheduler]
         * @param workManager Work manager
         */
        class Impl @Inject constructor(private val workManager: WorkManager) : Scheduler {
            /**
             * Schedules a task to upload a push token
             * @param token Token to upload
             */
            override fun invoke(token: FcmToken) {
                workManager.enqueueUniqueWork(
                    uniqueWorkName = WORK_NAME,
                    existingWorkPolicy = ExistingWorkPolicy.REPLACE,
                    request = buildTask(token)
                )
            }
        }
    }

    companion object {
        private const val KEY_TOKEN = "token"
        private const val WORK_NAME = "UploadTokenWorker"

        /**
         * Builds a task to upload a push token
         * @param token Token to upload
         */
        fun buildTask(token: FcmToken): OneTimeWorkRequest {
            val data = Data.Builder()
                .putString(KEY_TOKEN, token.value)
                .build()

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            return OneTimeWorkRequestBuilder<UploadTokenWorker>()
                .setInputData(data)
                .setConstraints(constraints)
                .build()
        }
    }
}
