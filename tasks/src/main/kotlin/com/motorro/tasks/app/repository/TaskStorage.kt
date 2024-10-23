package com.motorro.tasks.app.repository

import com.motorro.tasks.data.Task
import com.motorro.tasks.data.TaskUpdates
import com.motorro.tasks.data.UserName
import com.motorro.tasks.data.Version
import kotlinx.coroutines.flow.Flow

/**
 * Task data
 */
interface ReadonlyTasks {
    /**
     * Latest version
     * @param userName Bound user
     */
    fun getVersion(userName: UserName): Flow<Version?>

    /**
     * Flow of tasks
     * @param userName Bound user
     */
    fun getTasks(userName: UserName): Flow<Collection<Task>>
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
    suspend fun update(userName: UserName, update: TaskUpdates): Version
}



