package com.motorro.tasks.server

import com.motorro.tasks.data.Task
import com.motorro.tasks.data.TaskId
import com.motorro.tasks.data.UserName
import java.util.UUID

val user = UserName("user")

fun createTask(): Task = Task(
    id = TaskId(UUID.randomUUID().toString()),
    author = user,
    title = "New Task",
    description = "New Task Description",
    complete = false
)