package ru.merionet.tasks.app.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ru.merionet.core.coroutines.DispatcherProvider
import ru.merionet.tasks.app.repository.db.TasksDao
import ru.merionet.tasks.app.repository.db.entity.toTask
import ru.merionet.tasks.data.Task
import ru.merionet.tasks.data.TaskUpdates
import ru.merionet.tasks.data.UserName
import ru.merionet.tasks.data.Version
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
    override suspend fun update(userName: UserName, update: TaskUpdates) {
        withContext(dispatchers.io) {
            dao.update(userName, update)
        }
    }
}