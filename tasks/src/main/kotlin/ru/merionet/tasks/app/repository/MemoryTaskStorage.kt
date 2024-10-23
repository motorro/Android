package ru.merionet.tasks.app.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ru.merionet.core.coroutines.DispatcherProvider
import ru.merionet.core.log.Logging
import ru.merionet.tasks.data.Task
import ru.merionet.tasks.data.TaskCommand
import ru.merionet.tasks.data.TaskId
import ru.merionet.tasks.data.TaskUpdates
import ru.merionet.tasks.data.UserName
import ru.merionet.tasks.data.Version
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
    override suspend fun update(userName: UserName, update: TaskUpdates) {
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
