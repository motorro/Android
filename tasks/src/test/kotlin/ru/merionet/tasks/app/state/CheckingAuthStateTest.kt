package ru.merionet.tasks.app.state

import io.mockk.Ordering
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.test.runTest
import org.junit.Test
import ru.merionet.core.lce.LceState
import ru.merionet.tasks.activeSession
import ru.merionet.tasks.app.data.AppGesture
import ru.merionet.tasks.app.data.AppUiState
import ru.merionet.tasks.auth.SessionManager
import ru.merionet.tasks.auth.data.Session
import ru.merionet.tasks.auth.data.SessionError
import java.io.IOException

internal class CheckingAuthStateTest : BaseStateTest() {
    private val sessionManager: SessionManager = mockk()
    private val state: AppState = CheckingAuthState(context, sessionManager)

    @Test
    fun ifActiveTransfersToTaskList() = runTest(dispatcher) {
        every { factory.taskList(any()) } returns nextState
        every { sessionManager.session } returns flowOf(
            LceState.Loading(),
            LceState.Content(activeSession)
        ).stateIn(this, SharingStarted.Lazily, LceState.Loading())

        state.start(stateMachine)

        verify {
            stateMachine.setUiState(AppUiState.Loading())
            factory.taskList(appData)
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun ifNotActiveTransfersToLogin() = runTest(dispatcher) {
        every { factory.loggingIn(any()) } returns nextState
        every { sessionManager.session } returns flowOf(
            LceState.Loading(),
            LceState.Content(Session.NONE)
        ).stateIn(this, SharingStarted.Lazily, LceState.Loading())

        state.start(stateMachine)

        verify {
            stateMachine.setUiState(AppUiState.Loading())
            factory.loggingIn()
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun transfersToErrorOnError() = runTest(dispatcher) {
        val error = SessionError.Storage(IOException("Error"))
        every { factory.initError(any()) } returns nextState
        every { sessionManager.session } returns flowOf<LceState<Session, SessionError>>(
            LceState.Loading(),
            LceState.Error(error)
        ).stateIn(this, SharingStarted.Lazily, LceState.Loading())

        state.start(stateMachine)

        verify {
            stateMachine.setUiState(AppUiState.Loading())
            factory.initError(error)
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun exitsOnBack() = runTest(dispatcher) {
        every { sessionManager.session } returns flowOf<LceState<Session, SessionError>>(LceState.Loading()).stateIn(this)
        every { factory.terminated() } returns nextState

        state.start(stateMachine)
        state.process(AppGesture.Back)
        verify(ordering = Ordering.ORDERED) {
            factory.terminated()
            stateMachine.setMachineState(nextState)
        }
    }
}