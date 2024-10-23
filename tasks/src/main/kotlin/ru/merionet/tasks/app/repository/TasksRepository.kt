package ru.merionet.tasks.app.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import ru.merionet.core.lce.LceState
import ru.merionet.core.log.Logging
import ru.merionet.tasks.app.data.AppError
import ru.merionet.tasks.app.data.toApp
import ru.merionet.tasks.app.net.TasksApi
import ru.merionet.tasks.app.net.netResult
import ru.merionet.tasks.data.ErrorCode
import ru.merionet.tasks.data.Task
import ru.merionet.tasks.data.TaskCommand
import ru.merionet.tasks.data.TaskId
import ru.merionet.tasks.data.TaskUpdateRequest
import ru.merionet.tasks.data.TaskUpdates
import ru.merionet.tasks.data.UserName
import ru.merionet.tasks.data.Version
import ru.merionet.tasks.data.nextVersion
import javax.inject.Inject

/**
 * Tasks data repository
 */
interface TasksRepository : ReadonlyTasks {
    /**
     * Update task list from server
     * @param userName Bound user
     */
    fun update(userName: UserName): Flow<LceState<Unit, AppError>>

    /**
     * Refreshes task list from server
     * @param userName Bound user
     */
    fun refresh(userName: UserName): Flow<LceState<Unit, AppError>>

    /**
     * Insert / update
     * @param userName Bound user
     * @param task Task to update
     */
    suspend fun upsertTask(userName: UserName, task: Task)

    /**
     * Delete task
     * @param userName Bound user
     * @param taskId Task ID
     */
    suspend fun deleteTask(userName: UserName, taskId: TaskId)

    /**
     * Repository implementation (memory caching)
     */
    class Impl @Inject constructor(
        private val tasksApi: TasksApi,
        private val storage: ReadWriteTasks
    ) : TasksRepository, ReadonlyTasks by storage, Logging {
        /**
         * Update task list from server
         */
        override fun update(userName: UserName): Flow<LceState<Unit, AppError>> = flow {
            d { "Updating..." }
            emitAll(load(userName, storage.getVersion(userName).firstOrNull()))
        }

        /**
         * Refresh task list
         */
        override fun refresh(userName: UserName): Flow<LceState<Unit, AppError>> {
            d { "Refreshing..." }
            return load(userName, null)
        }

        /**
         * Loads updates from server building local task list
         */
        private fun load(userName: UserName, version: Version?): Flow<LceState<Unit, AppError>> = flow {
            emit(LceState.Loading(Unit))

            doLoad(userName, version).getOrElse {
                emit(LceState.Error(it.toApp(), Unit))
                return@flow
            }

            emit(LceState.Content(Unit))
        }

        /**
         * Does the update
         */
        private suspend fun doLoad(userName: UserName, version: Version?): Result<Version> {
            d { "Updating task list..." }
            return netResult { tasksApi.getUpdates(version) }.mapCatching { changes -> storage.update(userName, changes) }
        }

        /**
         * Insert / update
         */
        override suspend fun upsertTask(userName: UserName, task: Task) {
            d { "Upserting task: $task" }
            updateServer(userName, listOf(TaskCommand.Upsert(task)))
        }

        /**
         * Delete task
         * @param taskId Task ID
         */
        override suspend fun deleteTask(userName: UserName, taskId: TaskId) {
            d { "Deleting task: $taskId" }
            updateServer(userName, listOf(TaskCommand.Delete(taskId)))
        }

        /**
         * Runs update from server
         * @param userName Bound user
         * @param commands Commands to run
         * @param currentVersion Current version override
         */
        private suspend fun updateServer(
            userName: UserName,
            commands: List<TaskCommand>,
            currentVersion: Version? = null
        ) {
            // Loads data and retries the update
            suspend fun syncAndRetry(version: Version?) {
                updateServer(
                    userName,
                    commands,
                    doLoad(userName, version).getOrThrow()
                )
            }

            val effectiveCurrentVersion = currentVersion ?: storage.getVersion(userName).firstOrNull()
            if (null == effectiveCurrentVersion) {
                d { "Empty version. Updating data..." }
                syncAndRetry(null)
                return
            }

            val newVersion = nextVersion()
            d { "Updating server" }
            d { "Current version: $currentVersion" }
            d { "New version: $newVersion" }

            val update = netResult { tasksApi.postUpdates(TaskUpdateRequest(effectiveCurrentVersion, newVersion, commands)) }
                .recover { error ->
                    w(error) { "Error updating tasks" }
                    if (error is AppError.WorkFlow && ErrorCode.CONFLICT == error.code) {
                        d { "Data conflict. Retrying after data sync" }
                        syncAndRetry(effectiveCurrentVersion)
                        return
                    }
                    throw error
                }
                .getOrThrow()

            storage.update(userName, TaskUpdates(update.latestVersion, commands))
        }
    }
}



