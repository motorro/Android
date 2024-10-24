package ru.merionet.tasks.app.state

import io.mockk.Ordering
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import org.junit.Test
import ru.merionet.tasks.app.data.AppGesture
import ru.merionet.tasks.app.data.AppUiState
import ru.merionet.tasks.app.task1
import ru.merionet.tasks.data.Task
import kotlin.test.assertEquals
import kotlin.test.assertIs

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

        verify(ordering = Ordering.ORDERED) {
            stateMachine.setUiState(withArg {
                assertIs<AppUiState.EditTask>(it)
                assertEquals(task1.title, it.title)
            })
            stateMachine.setUiState(withArg {
                assertIs<AppUiState.EditTask>(it)
                assertEquals(newTitle, it.title)
            })
        }
    }

    @Test
    fun changesDescription() {
        val newDescription = "new description"
        val state = createState(task1)
        state.start(stateMachine)
        state.process(AppGesture.EditTask.DescriptionChanged(newDescription))

        verify(ordering = Ordering.ORDERED) {
            stateMachine.setUiState(withArg {
                assertIs<AppUiState.EditTask>(it)
                assertEquals(task1.description, it.description)
            })
            stateMachine.setUiState(withArg {
                assertIs<AppUiState.EditTask>(it)
                assertEquals(newDescription, it.description)
            })
        }
    }

    @Test
    fun changesDate() {
        val newDate = LocalDate(2024, 10, 25)
        val state = createState(task1)
        state.start(stateMachine)
        state.process(AppGesture.EditTask.DateSelected(newDate))

        verify(ordering = Ordering.ORDERED) {
            stateMachine.setUiState(withArg {
                assertIs<AppUiState.EditTask>(it)
                assertEquals(task1.due?.date, it.due?.date)
            })
            stateMachine.setUiState(withArg {
                assertIs<AppUiState.EditTask>(it)
                assertEquals(newDate, it.due?.date)
            })
        }
    }

    @Test
    fun doesNotChangeInvalidDate() {
        val newDate = LocalDate(2023, 10, 25)
        val state = createState(task1)
        state.start(stateMachine)
        state.process(AppGesture.EditTask.DateSelected(newDate))

        verify(exactly = 0) {
            stateMachine.setUiState(withArg {
                assertIs<AppUiState.EditTask>(it)
                assertEquals(newDate, it.due?.date)
            })
        }
    }

    @Test
    fun changesTime() {
        val newTime = LocalTime(23, 0)
        val state = createState(task1)
        state.start(stateMachine)
        state.process(AppGesture.EditTask.TimeSelected(newTime))

        verify(ordering = Ordering.ORDERED) {
            stateMachine.setUiState(withArg {
                assertIs<AppUiState.EditTask>(it)
                assertEquals(task1.due?.time, it.due?.time)
            })
            stateMachine.setUiState(withArg {
                assertIs<AppUiState.EditTask>(it)
                assertEquals(newTime, it.due?.time)
            })
        }
    }

    @Test
    fun doesNotChangeInvalidTime() {
        val newTime = LocalTime(9, 0)
        val state = createState(task1)
        state.start(stateMachine)
        state.process(AppGesture.EditTask.TimeSelected(newTime))

        verify(exactly = 0) {
            stateMachine.setUiState(withArg {
                assertIs<AppUiState.EditTask>(it)
                assertEquals(newTime, it.due?.time)
            })
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

        verify(ordering = Ordering.ORDERED) {
            factory.taskList(appData)
            stateMachine.setMachineState(nextState)
        }
    }
}