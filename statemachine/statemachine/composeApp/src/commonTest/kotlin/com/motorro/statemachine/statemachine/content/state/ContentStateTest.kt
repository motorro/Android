package com.motorro.statemachine.statemachine.content.state

import com.motorro.statemachine.common.session.data.Session
import com.motorro.statemachine.statemachine.BaseStateTest
import com.motorro.statemachine.statemachine.data.AppGesture
import com.motorro.statemachine.statemachine.data.ContentGesture
import com.motorro.statemachine.statemachine.data.ContentUiState
import com.motorro.statemachine.statemachine.user
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlin.test.Test

class ContentStateTest : BaseStateTest() {

    val state by withMocks { ContentState(stateFactory, sessionManager) }

    @Test
    fun displaysContentIfThereIsActiveUser() = test {
        every { sessionManager.session } returns flowOf(Session.Active(user)).stateIn(backgroundScope)

        state.start(stateMachine)

        verify {
            sessionManager.session
            stateMachine.setUiState(ContentUiState(user.username))
        }
    }

    @Test
    fun movesToLoginIfThereIsNoUser() = test {
        every { sessionManager.session } returns flowOf(Session.NotLoggedIn).stateIn(backgroundScope)
        every { stateFactory.authenticating() } returns nextState

        state.start(stateMachine)

        verify {
            sessionManager.session
            stateFactory.authenticating()
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun logsOutOnLogout() = test {
        every { sessionManager.session } returns flowOf(Session.Active(user)).stateIn(backgroundScope)
        every { stateFactory.loggingOut() } returns nextState

        state.start(stateMachine)
        state.process(ContentGesture.Logout)

        verify {
            sessionManager.session
            stateMachine.setUiState(ContentUiState(user.username))
            stateFactory.loggingOut()
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun terminatesOnBack() = test {
        every { sessionManager.session } returns flowOf(Session.Active(user)).stateIn(backgroundScope)
        every { stateFactory.terminated() } returns nextState

        state.start(stateMachine)
        state.process(AppGesture.Back)

        verify {
            sessionManager.session
            stateMachine.setUiState(ContentUiState(user.username))
            stateFactory.terminated()
            stateMachine.setMachineState(nextState)
        }
    }
}