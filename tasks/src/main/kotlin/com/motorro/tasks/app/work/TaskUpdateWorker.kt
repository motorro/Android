package com.motorro.tasks.app.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.motorro.core.error.WithRetry
import com.motorro.core.log.Logging
import com.motorro.tasks.app.repository.TasksRepository
import com.motorro.tasks.data.TaskCommand
import com.motorro.tasks.data.UserName
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Worker to sync tasks on background
 */
@HiltWorker
class TaskUpdateWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: TasksRepository
) : CoroutineWorker(appContext, workerParams), Logging {

    /**
     * Runs the task
     */
    override suspend fun doWork(): Result {
        val userName: UserName = requireNotNull(inputData.getString(PARAM_USERNAME)?.let(::UserName)) {
            "Username is required to do the job"
        }
        val commands: List<TaskCommand> = requireNotNull(inputData.getString(PARAM_COMMANDS)?.let { Json.decodeFromString(it) }) {
            "Commands are required to do the job"
        }

        return runCatching { repository.runUpdates(userName, commands) }
            .map {
                d { "Tasks updated successfully" }
                Result.success()
            }
            .getOrElse { error ->
                w(error) { "Update failed" }
                when (error) {
                    is WithRetry -> if (error.retriable) {
                        Result.retry()
                    } else {
                        w { "" }
                        Result.failure()
                    }
                    else -> {
                        Result.failure()
                    }
                }
            }
    }

    /**
     * Schedules task updates
     */
    interface Scheduler {
        /**
         * Schedules task updates
         * @param userName Bound user
         * @param commands Task update commands
         */
        fun schedule(userName: UserName, commands: List<TaskCommand>)

        class Impl @Inject constructor(@param:ApplicationContext private val context: Context) : Scheduler {
            override fun schedule(userName: UserName, commands: List<TaskCommand>) {
                WorkManager.getInstance(context).enqueueUniqueWork(
                    // Unique name for each user
                    buildUniqueName(userName),
                    // Append this work to previously scheduled with the same name
                    ExistingWorkPolicy.APPEND,
                    buildRequest(userName, commands)
                )
            }

            companion object {
                private fun buildUniqueName(userName: UserName) = "${TAG}_$userName"
            }
        }
    }

    companion object {
        /**
         * Builds update request
         * @param userName Bound user
         * @param commands Changes to send
         */
        fun buildRequest(
            userName: UserName,
            commands: List<TaskCommand>
        ): OneTimeWorkRequest = OneTimeWorkRequestBuilder<TaskUpdateWorker>()
            // Marks all works with tag
            .addTag(TAG)
            // Runs only when connection is present
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            // If non-fatal error occurs schedules a work for retry
            .setBackoffCriteria(
                BackoffPolicy.EXPONENTIAL,
                WorkRequest.DEFAULT_BACKOFF_DELAY_MILLIS,
                TimeUnit.MILLISECONDS
            )
            // Work data
            .setInputData(workDataOf(
                PARAM_USERNAME to userName.value,
                PARAM_COMMANDS to Json.encodeToString(ListSerializer(TaskCommand.serializer()), commands)
            ))
            .build()

        const val TAG = "UpdateTasks"
        const val PARAM_USERNAME: String = "userName"
        const val PARAM_COMMANDS: String = "commands"
    }
}