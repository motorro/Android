package ru.merionet.tasks.app.repository

import kotlinx.coroutines.flow.StateFlow
import ru.merionet.tasks.data.Task
import ru.merionet.tasks.data.TaskUpdates
import ru.merionet.tasks.data.UserName
import ru.merionet.tasks.data.Version

/**
 * Task data
 */
interface ReadonlyTasks {
    /**
     * Latest version
     * @param userName Bound user
     */
    fun getVersion(userName: UserName): StateFlow<Version?>

    /**
     * Flow of tasks
     * @param userName Bound user
     */
    fun getTasks(userName: UserName): StateFlow<Collection<Task>>
}

/**
 * Reactive task data storage
 * - Emits tasks on tasks subscription
 * - Updates subscribers whenever tasks update
 */
interface ReadWriteTasks : ReadonlyTasks {

    /**
     * Updates storage with new data
     * @param userName Active user
     * @param update Task updates
     */
    suspend fun update(userName: UserName, update: TaskUpdates)

    /**
     * Stores tasks in memory
     */
    class Memory
}



