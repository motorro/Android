package ru.merionet.tasks.server

import ru.merionet.tasks.data.Task
import ru.merionet.tasks.data.TaskId
import ru.merionet.tasks.data.UserName
import java.util.UUID

val user = UserName("user")

fun createTask(): Task = Task(
    id = TaskId(UUID.randomUUID().toString()),
    author = user,
    title = "New Task",
    description = "New Task Description",
    complete = false
)