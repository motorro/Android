package com.motorro.tasks.app.repository

import com.motorro.core.coroutines.DispatcherProvider
import com.motorro.tasks.app.repository.db.TasksDao
import com.motorro.tasks.app.repository.db.entity.toTask
import com.motorro.tasks.data.Task
import com.motorro.tasks.data.TaskUpdates
import com.motorro.tasks.data.UserName
import com.motorro.tasks.data.Version
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Stores tasks in memory
 */
class DbTaskStorage @Inject constructor(
    private val dao: TasksDao,
    private val dispatchers: DispatcherProvider
) : ReadWriteTasks {
    /**
     * Latest version for current user
     */
    override fun getVersion(userName: UserName): Flow<Version?> = dao.getVersion(userName)
        .map { it.firstOrNull() }
        .flowOn(dispatchers.io)

    /**
     * Flow of tasks
     */
    override fun getTasks(userName: UserName): Flow<Collection<Task>> = dao.getTasks(userName)
        .flowOn(dispatchers.io)
        .map { list -> list.map { it.toTask() } }

    /**
     * Updates storage with new data
     */
    override suspend fun update(userName: UserName, update: TaskUpdates): Version {
        withContext(dispatchers.io) {
            dao.update(userName, update)
        }
        return update.latestVersion
    }
}