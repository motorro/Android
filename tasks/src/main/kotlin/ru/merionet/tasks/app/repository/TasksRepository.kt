package ru.merionet.tasks.app.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.merionet.core.lce.LceState
import ru.merionet.core.log.Logging
import ru.merionet.tasks.app.data.AppError
import ru.merionet.tasks.app.data.toApp
import ru.merionet.tasks.app.net.TasksApi
import ru.merionet.tasks.app.net.netResult
import ru.merionet.tasks.data.Task
import ru.merionet.tasks.data.TaskId
import ru.merionet.tasks.data.UserName
import ru.merionet.tasks.data.Version
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
    fun upsertTask(userName: UserName, task: Task): Flow<LceState<Unit, AppError>>

    /**
     * Delete task
     * @param userName Bound user
     * @param taskId Task ID
     */
    fun deleteTask(userName: UserName, taskId: TaskId): Flow<LceState<Unit, AppError>>

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
        override fun update(userName: UserName): Flow<LceState<Unit, AppError>> {
            d { "Updating..." }
            return load(userName, storage.getVersion(userName).value)
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

            d { "Updating task list..." }
            val changes = netResult { tasksApi.getUpdates(version) }.getOrElse {
                emit(LceState.Error(it.toApp(), Unit))
                return@flow
            }

            storage.update(userName, changes)

            emit(LceState.Content(Unit))
        }

        /**
         * Insert / update
         */
        override fun upsertTask(userName: UserName, task: Task): Flow<LceState<Unit, AppError>> {
            TODO("Not yet implemented")
        }

        /**
         * Delete task
         * @param taskId Task ID
         */
        override fun deleteTask(userName: UserName, taskId: TaskId): Flow<LceState<Unit, AppError>> {
            TODO("Not yet implemented")
        }
    }
}



