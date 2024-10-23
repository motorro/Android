package com.motorro.tasks.app.repository

import com.motorro.core.coroutines.DispatcherProvider
import com.motorro.core.log.Logging
import com.motorro.tasks.data.Task
import com.motorro.tasks.data.TaskCommand
import com.motorro.tasks.data.TaskId
import com.motorro.tasks.data.TaskUpdates
import com.motorro.tasks.data.UserName
import com.motorro.tasks.data.Version
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MemoryTaskStorage  @Inject constructor(private val dispatchers: DispatcherProvider) : ReadWriteTasks, Logging {
    /**
     * Combined tasks
     */
    private val tasks: MutableStateFlow<Map<UserName, Data>> = MutableStateFlow(emptyMap())

    /**
     * Latest version
     */
    override fun getVersion(userName: UserName): Flow<Version?> = tasks.map {
        it[userName]?.version
    }

    /**
     * Flow of tasks
     */
    override fun getTasks(userName: UserName): Flow<Collection<Task>> = tasks.map {
        it[userName]?.tasks?.values.orEmpty()
    }

    /**
     * Updates storage with new data
     */
    override suspend fun update(userName: UserName, update: TaskUpdates): Version {
        d { "Updating tasks..." }
        val data = tasks.value.toMutableMap()
        val tasks = data[userName]?.tasks.orEmpty().toMutableMap()
        withContext(dispatchers.default) {
            update.commands.forEach { change ->
                when(change) {
                    is TaskCommand.Upsert -> {
                        d { "Upserting ${change.task.id}..." }
                        tasks[change.task.id] = change.task
                    }
                    is TaskCommand.Delete -> {
                        d { "Deleting ${change.id}..." }
                        tasks.remove(change.id)
                    }
                }
            }
        }
        d { "Processing complete" }

        data[userName] = Data(
            update.latestVersion,
            tasks
        )

        this.tasks.emit(data)
        return update.latestVersion
    }
}

/**
 * Data storage
 * @property version Current data version
 * @property tasks Loaded tasks
 */
private data class Data(
    val version: Version?,
    val tasks: Map<TaskId, Task>
)
