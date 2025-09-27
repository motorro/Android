package com.motorro.cookbook.recipelist.state

import com.motorro.cookbook.domain.session.SessionManager
import com.motorro.cookbook.recipelist.data.RecipeListViewState
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

internal class LoggingOutStateTest : BaseStateTest() {

    private lateinit var sessionManager: SessionManager
    private lateinit var state: RecipeListState

    override fun doInit() {
        sessionManager = mockk {
            coEvery { logout() } just Runs
        }
        every { factory.init() } returns nextState

        state = LoggingOutState(context, sessionManager)
    }

    @Test
    fun initialStateIsContentWithLoading() = test {
        state.start(stateMachine)

        verify {
            stateMachine.setUiState(RecipeListViewState.Loading)
        }
    }

    @Test
    fun logsOutAndStartsFromScratch() = test {
        state.start(stateMachine)

        coVerify {
            sessionManager.logout()
            factory.init()
            stateMachine.setMachineState(nextState)
        }
    }
}