package com.motorro.tasks.app.state

import com.motorro.tasks.app.data.AppGesture
import com.motorro.tasks.app.data.AppUiState
import com.motorro.tasks.app.task1
import com.motorro.tasks.data.Task
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Clock

internal class TaskStateTest : BaseStateTest() {
    private val clock: Clock = mockk {
        every { this@mockk.now() } returns LocalDateTime(
            2024,
            10,
            24,
            10,
            19
        ).toInstant(TimeZone.UTC)
    }
    private fun createState(task: Task? = null) = TaskState(
        context,
        appData,
        task,
        clock,
        TimeZone.UTC
    )

    @Test
    fun rendersTaskOnStart() {
        val state = createState(task1)
        state.start(stateMachine)

        verify {
            stateMachine.setUiState(
                AppUiState.EditTask(
                    title = task1.title,
                    description = task1.description,
                    due = task1.due,
                    completeVisible = true,
                    saveEnabled = false
                )
            )
        }
    }

    @Test
    fun changesTitle() {
        val newTitle = "new title"
        val state = createState(task1)
        state.start(stateMachine)
        state.process(AppGesture.EditTask.TitleChanged(newTitle))

        val updates = mutableListOf<AppUiState>()
        verify {
            stateMachine.setUiState(capture(updates))
        }

        assertEquals(task1.title, (updates.first() as AppUiState.EditTask).title)
        assertEquals(newTitle, (updates.last() as AppUiState.EditTask).title)
    }

    @Test
    fun changesDescription() {
        val newDescription = "new description"
        val state = createState(task1)
        state.start(stateMachine)
        state.process(AppGesture.EditTask.DescriptionChanged(newDescription))

        val updates = mutableListOf<AppUiState>()
        verify {
            stateMachine.setUiState(capture(updates))
        }

        assertEquals(task1.description, (updates.first() as AppUiState.EditTask).description)
        assertEquals(newDescription, (updates.last() as AppUiState.EditTask).description)
    }

    @Test
    fun changesDate() {
        val newDate = LocalDate(2024, 10, 25)
        val state = createState(task1)
        state.start(stateMachine)
        state.process(AppGesture.EditTask.DateSelected(newDate))

        val updates = mutableListOf<AppUiState>()
        verify {
            stateMachine.setUiState(capture(updates))
        }

        assertEquals(task1.due?.date, (updates.first() as AppUiState.EditTask).due?.date)
        assertEquals(newDate, (updates.last() as AppUiState.EditTask).due?.date)
    }

    @Test
    fun doesNotChangeInvalidDate() {
        val newDate = LocalDate(2023, 10, 25)
        val state = createState(task1)
        state.start(stateMachine)
        state.process(AppGesture.EditTask.DateSelected(newDate))

        val updates = mutableListOf<AppUiState>()
        verify {
            stateMachine.setUiState(capture(updates))
        }

        assertTrue {
            updates.none {
                it is AppUiState.EditTask && it.due?.date == newDate
            }
        }
    }

    @Test
    fun changesTime() {
        val newTime = LocalTime(23, 0)
        val state = createState(task1)
        state.start(stateMachine)
        state.process(AppGesture.EditTask.TimeSelected(newTime))

        val updates = mutableListOf<AppUiState>()
        verify {
            stateMachine.setUiState(capture(updates))
        }

        assertEquals(task1.due?.time, (updates.first() as AppUiState.EditTask).due?.time)
        assertEquals(newTime, (updates.last() as AppUiState.EditTask).due?.time)
    }

    @Test
    fun doesNotChangeInvalidTime() {
        val newTime = LocalTime(9, 0)
        val state = createState(task1)
        state.start(stateMachine)
        state.process(AppGesture.EditTask.TimeSelected(newTime))

        val updates = mutableListOf<AppUiState>()
        verify {
            stateMachine.setUiState(capture(updates))
        }

        assertTrue {
            updates.none {
                it is AppUiState.EditTask && it.due?.time == newTime
            }
        }
    }

    @Test
    fun completesTask() {
        every { factory.savingTask(any(), any()) } returns nextState
        val state = createState(task1)
        state.start(stateMachine)
        state.process(AppGesture.EditTask.CompleteClicked)

        verify {
            factory.savingTask(appData, task1.copy(complete = true))
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun doesNotCompleteEmptyTask() {
        every { factory.savingTask(any(), any()) } returns nextState
        val state = createState()
        state.start(stateMachine)
        state.process(AppGesture.EditTask.CompleteClicked)

        verify(exactly = 0) {
            factory.savingTask(any(), any())
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun savesTaskWithTitle() {
        every { factory.savingTask(any(), any()) } returns nextState
        val title = "Title"
        val state = createState()
        state.start(stateMachine)
        state.process(AppGesture.EditTask.TitleChanged(title))
        state.process(AppGesture.EditTask.CompleteClicked)

        verify {
            factory.savingTask(any(), withArg {
                assertEquals(title, it.title)
            })
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun returnsToListOnBack() {
        every { factory.taskList(any()) } returns nextState
        val state = createState()

        state.start(stateMachine)
        state.process(AppGesture.Back)

        verify {
            factory.taskList(appData)
            stateMachine.setMachineState(nextState)
        }
    }
}