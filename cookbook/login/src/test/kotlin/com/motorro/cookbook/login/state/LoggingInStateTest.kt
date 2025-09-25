package com.motorro.cookbook.login.state

import com.motorro.cookbook.domain.session.SessionManager
import com.motorro.cookbook.login.data.LoginGesture
import com.motorro.cookbook.login.data.LoginViewState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import kotlin.coroutines.suspendCoroutine
import kotlin.test.assertIs

@OptIn(ExperimentalCoroutinesApi::class)
internal class LoggingInStateTest : BaseStateTest() {

    private lateinit var sessionManager: SessionManager
    private lateinit var state: LoginState

    override fun doInit() {
        sessionManager = mockk()
        state = LoggingInState(
            context,
            DATA,
            sessionManager
        )
    }

    @Test
    fun setsLoadingUiStateOnStart() = test {
        coEvery { sessionManager.login(any(), any()) } coAnswers {
            suspendCoroutine {
                // Do nothing
            }
        }

        state.start(stateMachine)

        verify {
            stateMachine.setUiState(withArg {
                assertIs<LoginViewState.Loading>(it)
            })
        }
    }

    @Test
    fun transitionsToCompleteOnSuccessfulLogin() = test {
        every { factory.complete(any()) } returns nextState
        coEvery { sessionManager.login(DATA.username, DATA.password) } returns Result.success(PROFILE)

        state.start(stateMachine)

        coVerify { sessionManager.login(DATA.username, DATA.password) }
        verify { factory.complete(PROFILE) }
        verify { stateMachine.setMachineState(nextState) }
    }

    @Test
    fun transitionsToFormOnError() = test {
        every { factory.form(any(), anyNullable()) } returns nextState
        coEvery { sessionManager.login(any(), any()) } returns Result.failure(ERROR)

        state.start(stateMachine)

        coVerify { sessionManager.login(DATA.username, DATA.password) }
        verify { factory.form(DATA, ERROR) }
        verify { stateMachine.setMachineState(nextState) }
    }

    @Test
    fun returnsToFormOnBack() = test {
        every { factory.form(any(), anyNullable()) } returns nextState
        coEvery { sessionManager.login(any(), any()) } coAnswers {
            suspendCoroutine {
                // Do nothing
            }
        }

        state.start(stateMachine)
        state.process(LoginGesture.Back)

        verify {
            factory.form(DATA)
            stateMachine.setMachineState(nextState)
        }
    }
}