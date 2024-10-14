package ru.merionet.tasks.login.state

import io.mockk.Ordering
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import ru.merionet.core.lce.LceState
import ru.merionet.tasks.activeSession
import ru.merionet.tasks.auth.SessionManager
import ru.merionet.tasks.auth.data.SessionError
import ru.merionet.tasks.data.ErrorCode
import ru.merionet.tasks.login.data.LoginGesture
import ru.merionet.tasks.login.data.LoginUiState

internal class LoggingInStateTest : BaseStateTest() {
    private val sessionManager: SessionManager = mockk()
    private val state: LoginState = LoggingInState(context, loginData, sessionManager)

    @Test
    fun logsInIfSuccessful() = runTest(dispatcher) {
        every { factory.authenticated(any(), any()) } returns nextState
        every { sessionManager.authenticate(any()) } returns flowOf(
            LceState.Loading(),
            LceState.Content(activeSession)
        )

        state.start(stateMachine)

        verify {
            stateMachine.setUiState(LoginUiState.Loading)
            factory.authenticated(loginData, activeSession.claims)
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun transfersToErrorOnError() = runTest(dispatcher) {
        val error = SessionError.Authentication(ErrorCode.FORBIDDEN, "Not allowed")
        every { factory.loginError(any(), any()) } returns nextState
        every { sessionManager.authenticate(any()) } returns flowOf(
            LceState.Loading(),
            LceState.Error(error)
        )

        state.start(stateMachine)

        verify {
            stateMachine.setUiState(LoginUiState.Loading)
            factory.loginError(loginData, error)
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun returnsToFormOnBack() = runTest(dispatcher) {
        every { sessionManager.authenticate(any()) } returns flow {
            // Waits endlessly
        }
        every { factory.form(any()) } returns nextState

        state.start(stateMachine)
        state.process(LoginGesture.Back)
        verify(ordering = Ordering.ORDERED) {
            factory.form(loginData)
            stateMachine.setMachineState(nextState)
        }
    }
}