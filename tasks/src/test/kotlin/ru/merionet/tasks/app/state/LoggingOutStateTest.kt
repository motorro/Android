package ru.merionet.tasks.app.state

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import ru.merionet.core.lce.LceState
import ru.merionet.tasks.USER_NAME
import ru.merionet.tasks.app.data.AppUiState
import ru.merionet.tasks.auth.SessionManager
import ru.merionet.tasks.auth.data.Session
import ru.merionet.tasks.auth.data.SessionError
import java.io.IOException

internal class LoggingOutStateTest : BaseStateTest() {
    private val sessionManager: SessionManager = mockk()
    private val state: AppState = LoggingOutState(context, appData, sessionManager)

    @Test
    fun transfersToLoginIfSuccessful() = runTest(dispatcher) {
        every { factory.loggingIn(any(), any()) } returns nextState
        every { sessionManager.logout() } returns flowOf(
            LceState.Loading(),
            LceState.Content(Session.NONE)
        )

        state.start(stateMachine)

        verify {
            stateMachine.setUiState(AppUiState.Loading())
            factory.loggingIn(USER_NAME, null)
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun transfersToErrorOnError() = runTest(dispatcher) {
        val error = SessionError.Network(IOException("Network error"))
        every { factory.logoutError(any(), any()) } returns nextState
        every { sessionManager.logout() } returns flowOf(
            LceState.Loading(),
            LceState.Error(error)
        )

        state.start(stateMachine)

        verify {
            stateMachine.setUiState(AppUiState.Loading())
            factory.logoutError(appData, error)
            stateMachine.setMachineState(nextState)
        }
    }
}