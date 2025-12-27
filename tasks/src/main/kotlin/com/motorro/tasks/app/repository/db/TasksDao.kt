package com.motorro.tasks.app.repository.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.motorro.tasks.app.repository.db.entity.TaskEntity
import com.motorro.tasks.app.repository.db.entity.TaskListEntity
import com.motorro.tasks.app.repository.db.entity.toEntity
import com.motorro.tasks.data.TaskCommand
import com.motorro.tasks.data.TaskId
import com.motorro.tasks.data.TaskUpdates
import com.motorro.tasks.data.UserName
import com.motorro.tasks.data.Version
import kotlinx.coroutines.flow.Flow

/**
 * Data access
 * Room takes method signatures and annotations and generates actual DB access functions
 */
@Dao
abstract class TasksDao {
    @Upsert
    abstract suspend fun upsertTask(task: TaskEntity)

    @Delete(entity = TaskEntity::class)
    abstract suspend fun deleteTask(id: TaskId)

    @Upsert
    abstract suspend fun upsertTaskList(list: TaskListEntity)

    @Query("DELETE FROM task_lists WHERE user_name = :userName")
    abstract suspend fun deleteTaskList(userName: UserName)

    @Query("SELECT latestVersion FROM task_lists WHERE user_name = :userName")
    abstract fun getVersion(userName: UserName): Flow<List<Version>>

    @Query("SELECT * FROM tasks WHERE tasks.author = :userName")
    abstract fun getTasks(userName: UserName): Flow<List<TaskEntity>>

    @Transaction
    open suspend fun update(userName: UserName, update: TaskUpdates) {
        upsertTaskList(TaskListEntity(userName, update.latestVersion))
        update.commands.forEach { command ->
            when(command) {
                is TaskCommand.Delete -> deleteTask(command.id)
                is TaskCommand.Upsert -> upsertTask(command.task.toEntity())
            }
        }
    }
}