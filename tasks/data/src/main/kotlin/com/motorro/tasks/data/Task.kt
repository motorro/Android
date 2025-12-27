package com.motorro.tasks.data

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import java.util.UUID

/**
 * Task id
 * Global unique identifier for a task
 */
@JvmInline
@Serializable
value class TaskId(val id: String)

/**
 * Creates next Task ID
 */
fun nextTaskId(): TaskId = TaskId(UUID.randomUUID().toString())

/**
 * Task data
 */
@Serializable
data class Task(
    val id: TaskId,
    val author: UserName,
    val title: String,
    val description: String,
    val complete: Boolean,
    val due: LocalDateTime? = null
)

/**
 * Creates new empty task
 */
fun newTaskTemplate(userName: UserName): Task = Task(
    id = nextTaskId(),
    author = userName,
    title = "",
    description = "",
    complete = false,
    due = null
)

/**
 * Task update batch
 * Server -> Client
 * @property latestVersion Latest version on server
 * @property commands Update commands
 */
@Serializable
data class TaskUpdates(
    val latestVersion: Version,
    val commands: List<TaskCommand>
)

/**
 * Task update request
 * @property expectedVersion Expected current version on server
 * @property nextVersion New version to set
 * @property commands Commands to add to history
 */
@Serializable
data class TaskUpdateRequest(
    val expectedVersion: Version,
    val nextVersion: Version,
    val commands: List<TaskCommand>
)

/**
 * Task update response
 */
@Serializable
data class VersionResponse(val latestVersion: Version)

/**
 * Task command
 */
@Serializable
sealed class TaskCommand {
    /**
     * Upsert a task
     */
    @Serializable
    data class Upsert(val task: Task) : TaskCommand()
    /**
     * Delete a task
     */
    @Serializable
    data class Delete(val id: TaskId) : TaskCommand()
}

