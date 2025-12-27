package com.motorro.tasks.app.state

import com.motorro.core.lce.LceState
import com.motorro.tasks.USER_NAME
import com.motorro.tasks.app.data.AppError
import com.motorro.tasks.app.data.AppGesture
import com.motorro.tasks.app.data.AppUiState
import com.motorro.tasks.app.repository.TasksRepository
import com.motorro.tasks.app.task1
import com.motorro.tasks.app.task2
import com.motorro.tasks.auth.data.SessionError
import com.motorro.tasks.data.ErrorCode
import io.mockk.Ordering
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.io.IOException

internal class TaskListStateTest : BaseStateTest() {
    private val tasksRepository: TasksRepository = mockk()
    private val state = TaskListState(context, appData, tasksRepository)

    @Test
    @Suppress("UnusedFlow")
    fun subscribesAndUpdatesTasksOnStart() = runTest(dispatcher) {
        every { tasksRepository.getTasks(any()) } returns flowOf(listOf(task1, task2)).stateIn(this)
        every { tasksRepository.update(any()) } returns flowOf(
            LceState.Loading(),
            LceState.Content(Unit)
        )

        state.start(stateMachine)

        coVerify {
            stateMachine.setUiState(
                AppUiState.TaskList(
                    listOf(task1, task2),
                    false,
                    null
                )
            )
            tasksRepository.getTasks(USER_NAME)
            tasksRepository.update(USER_NAME)
        }
    }

    @Test
    fun updatesTasksWhenRepositoryUpdates() = runTest(dispatcher) {
        val tasks = MutableStateFlow(listOf(task1))
        every { tasksRepository.getTasks(any()) } returns tasks
        every { tasksRepository.update(any()) } returns flowOf(
            LceState.Loading(),
            LceState.Content(Unit)
        )

        state.start(stateMachine)
        tasks.emit(listOf(task2))

        coVerify(ordering = Ordering.ORDERED) {
            stateMachine.setUiState(
                AppUiState.TaskList(
                    listOf(task1),
                    false,
                    null
                )
            )
            stateMachine.setUiState(
                AppUiState.TaskList(
                    listOf(task2),
                    false,
                    null
                )
            )
        }
    }

    @Test
    @Suppress("UnusedFlow")
    fun setsErrorIfUpdateFails() = runTest(dispatcher) {
        val error = AppError.Network(IOException())
        every { tasksRepository.getTasks(any()) } returns flowOf(listOf(task1, task2)).stateIn(this)
        every { tasksRepository.update(any()) } returns flowOf(
            LceState.Loading(),
            LceState.Error(error)
        )

        state.start(stateMachine)

        coVerify {
            stateMachine.setUiState(
                AppUiState.TaskList(
                    listOf(task1, task2),
                    false,
                    error
                )
            )
            tasksRepository.getTasks(USER_NAME)
            tasksRepository.update(USER_NAME)
        }
    }

    @Test
    @Suppress("UnusedFlow")
    fun dismissesRetriableErrorAndRefreshes() = runTest(dispatcher) {
        val error = AppError.Network(IOException())
        every { tasksRepository.getTasks(any()) } returns flowOf(listOf(task1, task2)).stateIn(this)
        every { tasksRepository.update(any()) } returns flowOf(
            LceState.Loading(),
            LceState.Error(error)
        )

        state.start(stateMachine)
        coVerify {
            stateMachine.setUiState(
                AppUiState.TaskList(
                    listOf(task1, task2),
                    false,
                    error
                )
            )
            tasksRepository.getTasks(USER_NAME)
            tasksRepository.update(USER_NAME)
        }

        state.process(AppGesture.DismissError)
        coVerify(exactly = 2) { tasksRepository.update(USER_NAME) }
    }

    @Test
    @Suppress("UnusedFlow")
    fun quitsOnFatalError() = runTest(dispatcher) {
        val error = AppError.Storage(IOException())
        every { tasksRepository.getTasks(any()) } returns flowOf(listOf(task1, task2)).stateIn(this)
        every { tasksRepository.update(any()) } returns flowOf(
            LceState.Loading(),
            LceState.Error(error)
        )
        every { factory.terminated() } returns nextState

        state.start(stateMachine)
        coVerify {
            stateMachine.setUiState(
                AppUiState.TaskList(
                    listOf(task1, task2),
                    false,
                    error
                )
            )
            tasksRepository.getTasks(USER_NAME)
            tasksRepository.update(USER_NAME)
        }

        state.process(AppGesture.DismissError)
        coVerify {
            factory.terminated()
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    @Suppress("UnusedFlow")
    fun logsOutOnAuthError() = runTest(dispatcher) {
        val error = AppError.Authentication(SessionError.Authentication(ErrorCode.FORBIDDEN, ErrorCode.FORBIDDEN.defaultMessage))
        every { tasksRepository.getTasks(any()) } returns flowOf(listOf(task1, task2)).stateIn(this)
        every { tasksRepository.update(any()) } returns flowOf(
            LceState.Loading(),
            LceState.Error(error)
        )
        every { factory.loggingOut(any()) } returns nextState

        state.start(stateMachine)
        coVerify {
            stateMachine.setUiState(
                AppUiState.TaskList(
                    listOf(task1, task2),
                    false,
                    error
                )
            )
            tasksRepository.getTasks(USER_NAME)
            tasksRepository.update(USER_NAME)
        }

        state.process(AppGesture.DismissError)
        coVerify {
            factory.loggingOut(appData)
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    @Suppress("UnusedFlow")
    fun updatesOnRefresh() = runTest(dispatcher) {
        every { tasksRepository.getTasks(any()) } returns flowOf(listOf(task1, task2)).stateIn(this)
        every { tasksRepository.update(any()) } returns flowOf(
            LceState.Loading(),
            LceState.Content(Unit)
        )

        state.start(stateMachine)
        state.process(AppGesture.Refresh)

        coVerify(exactly = 2) {
            tasksRepository.update(USER_NAME)
        }
    }

    @Test
    fun addsTask() = runTest(dispatcher) {
        every { tasksRepository.getTasks(any()) } returns flowOf(listOf(task1, task2)).stateIn(this)
        every { tasksRepository.update(any()) } returns flowOf(
            LceState.Loading(),
            LceState.Content(Unit)
        )
        every { factory.task(any(), any()) } returns nextState

        state.start(stateMachine)
        state.process(AppGesture.TaskList.AddClicked)

        coVerify {
            factory.task(appData, null)
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun selectsTask() = runTest(dispatcher) {
        every { tasksRepository.getTasks(any()) } returns flowOf(listOf(task1, task2)).stateIn(this)
        every { tasksRepository.update(any()) } returns flowOf(
            LceState.Loading(),
            LceState.Content(Unit)
        )
        every { factory.task(any(), any()) } returns nextState

        state.start(stateMachine)
        state.process(AppGesture.TaskList.TaskClicked(task1.id))

        coVerify {
            factory.task(appData, task1)
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun exitsOnBack() = runTest(dispatcher) {
        every { tasksRepository.getTasks(any()) } returns flowOf(listOf(task1, task2)).stateIn(this)
        every { tasksRepository.update(any()) } returns flowOf(
            LceState.Loading(),
            LceState.Content(Unit)
        )
        every { factory.terminated() } returns nextState

        state.start(stateMachine)
        state.process(AppGesture.Back)

        coVerify {
            factory.terminated()
            stateMachine.setMachineState(nextState)
        }
    }
}