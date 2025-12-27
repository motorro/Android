package com.motorro.tasks.app.state

import com.motorro.core.lce.LceState
import com.motorro.tasks.USER_NAME
import com.motorro.tasks.app.data.AppUiState
import com.motorro.tasks.auth.SessionManager
import com.motorro.tasks.auth.data.Session
import com.motorro.tasks.auth.data.SessionError
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
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