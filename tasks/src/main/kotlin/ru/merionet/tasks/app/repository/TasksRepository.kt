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
import ru.merionet.tasks.app.work.TaskUpdateWorker
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
     * Insert / update with background sync
     * @param userName Bound user
     * @param task Task to update
     */
    suspend fun upsertTaskAsync(userName: UserName, task: Task)

    /**
     * Delete task
     * @param userName Bound user
     * @param taskId Task ID
     */
    suspend fun deleteTask(userName: UserName, taskId: TaskId)

    /**
     * Delete task with background sync
     * @param userName Bound user
     * @param taskId Task ID
     */
    suspend fun deleteTaskAsync(userName: UserName, taskId: TaskId)

    /**
     * Schedules updates to be synchronized
     * @param userName Bound user
     * @param commands Commands to run
     */
    suspend fun runUpdates(userName: UserName, commands: List<TaskCommand>)

    /**
     * Repository implementation (memory caching)
     */
    class Impl @Inject constructor(
        private val tasksApi: TasksApi,
        private val storage: ReadWriteTasks,
        private val updatesScheduler: TaskUpdateWorker.Scheduler
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
         * Insert / update with background sync
         * @param userName Bound user
         * @param task Task to update
         */
        override suspend fun upsertTaskAsync(userName: UserName, task: Task) {
            d { "Upserting task: $task" }
            registerUpdates(userName, listOf(TaskCommand.Upsert(task)))
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
         * Delete task with background sync
         * @param userName Bound user
         * @param taskId Task ID
         */
        override suspend fun deleteTaskAsync(userName: UserName, taskId: TaskId) {
            d { "Deleting task: $taskId" }
            registerUpdates(userName, listOf(TaskCommand.Delete(taskId)))
        }

        /**
         * Optimistically registers updates locally
         * and schedules background sever sync
         */
        private suspend fun registerUpdates(userName: UserName, commands: List<TaskCommand>) {
            val currentVersion = storage.getVersion(userName).firstOrNull()
            if (null == currentVersion) {
                d { "Empty version. Updating data..." }
                doLoad(userName, null).getOrThrow()
                registerUpdates(
                    userName,
                    commands
                )
                return
            }

            // Save to database (optimistic write)
            d { "Updating database..." }
            storage.update(
                userName,
                TaskUpdates(
                    currentVersion,
                    commands
                )
            )

            // Schedule updates to server
            d { "Scheduling server sync..." }
            updatesScheduler.schedule(userName, commands)
        }

        /**
         * Runs a batch of commands
         * @param userName Bound user
         * @param commands Commands to run
         */
        override suspend fun runUpdates(userName: UserName, commands: List<TaskCommand>) {
            d { "Running commands: ${commands.size}" }
            updateServer(userName, commands)
        }

        /**
         * Runs update from server
         * @param userName Bound user
         * @param commands Commands to run
         */
        private suspend fun updateServer(
            userName: UserName,
            commands: List<TaskCommand>
        ) {
            // Loads data and retries the update
            suspend fun syncAndRetry(version: Version?) {
                doLoad(userName, version).getOrThrow()
                updateServer(
                    userName,
                    commands
                )
            }

            val currentVersion = storage.getVersion(userName).firstOrNull()
            if (null == currentVersion) {
                d { "Empty version. Updating data..." }
                syncAndRetry(null)
                return
            }

            val newVersion = nextVersion()
            d { "Updating server" }
            d { "Current version: $currentVersion" }
            d { "New version: $newVersion" }

            val update = netResult { tasksApi.postUpdates(TaskUpdateRequest(currentVersion, newVersion, commands)) }
                .recover { error ->
                    w(error) { "Error updating tasks" }
                    if (error is AppError.WorkFlow && ErrorCode.CONFLICT == error.code) {
                        d { "Data conflict. Retrying after data sync" }
                        syncAndRetry(currentVersion)
                        return
                    }
                    throw error
                }
                .getOrThrow()

            storage.update(userName, TaskUpdates(update.latestVersion, commands))
        }
    }
}



