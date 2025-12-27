package com.motorro.tasks.app

import com.motorro.tasks.USER_NAME
import com.motorro.tasks.data.Task
import com.motorro.tasks.data.TaskId
import kotlinx.datetime.LocalDateTime

internal val task1 = Task(
    id = TaskId(id = "task1"),
    author = USER_NAME,
    title = "Task 1",
    description = "The first task",
    complete = false,
    due = LocalDateTime(
        2024,
        10,
        24,
        22,
        0
    )
)

internal val task2 = Task(
    id = TaskId(id = "task2"),
    author = USER_NAME,
    title = "Task 2",
    description = "The second task",
    complete = false
)