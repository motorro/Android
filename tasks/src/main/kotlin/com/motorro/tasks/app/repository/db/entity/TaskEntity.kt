package com.motorro.tasks.app.repository.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.motorro.tasks.data.Task
import com.motorro.tasks.data.TaskId
import com.motorro.tasks.data.UserName
import kotlinx.datetime.LocalDateTime

/**
 * Task data
 */
@Entity(
    tableName = "tasks",
    primaryKeys = ["id", "author"],
    foreignKeys = [
        ForeignKey(
            TaskListEntity::class,
            ["user_name"],
            ["author"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
            deferred = true
        )
    ]
)
data class TaskEntity(
    @ColumnInfo(name = "id")
    val id: TaskId,
    @ColumnInfo(name = "author", index = true)
    val author: UserName,
    val title: String,
    val description: String,
    val complete: Boolean,
    val due: LocalDateTime? = null
)

fun Task.toEntity() = TaskEntity(
    id,
    author,
    title,
    description,
    complete,
    due
)

fun TaskEntity.toTask() = Task(
    id,
    author,
    title,
    description,
    complete,
    due
)