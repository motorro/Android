package com.motorro.tasks.login.state

import com.motorro.tasks.login.data.LoginGesture
import com.motorro.tasks.login.data.LoginUiState
import io.mockk.Ordering
import io.mockk.every
import io.mockk.verify
import org.junit.Test

internal class LoggingInStateTest : BaseStateTest() {
    private val state: LoginState = LoggingInState(context, loginData)

    @Test
    fun rendersLoadingOnStart() {
        state.start(stateMachine)

        verify {
            stateMachine.setUiState(LoginUiState.Loading)
        }
    }

    @Test
    fun returnsToFormOnBack() {
        every { factory.form(any()) } returns nextState

        state.start(stateMachine)
        state.process(LoginGesture.Back)
        verify(ordering = Ordering.ORDERED) {
            factory.form(loginData)
            stateMachine.setMachineState(nextState)
        }
    }
}