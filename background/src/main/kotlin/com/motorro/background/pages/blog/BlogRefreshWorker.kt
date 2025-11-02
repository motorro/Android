package com.motorro.background.pages.blog

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkerParameters
import com.motorro.background.pages.blog.repository.BlogRepository
import com.motorro.core.log.Logging
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.time.Duration
import kotlin.time.Clock

@HiltWorker
class BlogRefreshWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val blogRepository: BlogRepository,
    private val clock: Clock
) : CoroutineWorker(appContext, params), Logging {

    companion object {
        const val TAG = "BlogRefreshWorker"

        /**
         * Schedules periodic job
         */
        fun createJob() = PeriodicWorkRequestBuilder<BlogRefreshWorker>(Duration.ofMillis(PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS))
            .addTag(TAG)
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
        i { "Updating blog posts: ${clock.now().toLocalDateTime(TimeZone.Companion.currentSystemDefault())}" }
        blogRepository.refresh()
        return Result.success()
    }
}