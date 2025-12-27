package com.motorro.tasks.app

import com.motorro.tasks.USER_NAME
import com.motorro.tasks.data.Task
import com.motorro.tasks.data.TaskId

internal val task1 = Task(
    id = TaskId(id = "task1"),
    author = USER_NAME,
    title = "Task 1",
    description = "The first task",
    complete = false
)

internal val task2 = Task(
    id = TaskId(id = "task2"),
    author = USER_NAME,
    title = "Task 2",
    description = "The second task",
    complete = false
)