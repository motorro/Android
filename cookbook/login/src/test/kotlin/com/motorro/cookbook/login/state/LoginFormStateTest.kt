package com.motorro.cookbook.login.state

import com.motorro.cookbook.login.data.LoginGesture
import com.motorro.cookbook.login.data.LoginViewState
import io.mockk.every
import io.mockk.verify
import io.mockk.verifyOrder
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

internal class LoginFormStateTest : BaseStateTest() {
    private fun createState(withError: Boolean = false) = LoginFormState(
        context = context,
        data = DATA,
        error = ERROR.takeIf { withError }
    )

    // region Static functions test
    @Test
    fun validIfUserAndPasswordAreNotBlank() {
        assertEquals(true, LoginFormState.isValid(DATA))
    }

    @Test
    fun invalidIfUserIsBlank() {
        assertEquals(false, LoginFormState.isValid(DATA.copy(username = "")))
    }

    @Test
    fun invalidIfPasswordIsBlank() {
        assertEquals(false, LoginFormState.isValid(DATA.copy(password = "")))
    }
    // endregion


    // region State test
    @Test
    fun rendersFormIfNoError() = test {
        val state = createState()
        state.start(stateMachine)

        verify {
            stateMachine.setUiState(withArg {
                assertIs<LoginViewState.Form>(it)
                assertEquals(DATA.username, it.username)
                assertEquals(DATA.password, it.password)
                assertTrue { it.loginEnabled }
            })
        }
    }

    @Test
    fun rendersErrorIfErrorPassed() = test {
        val state = createState(withError = true)
        state.start(stateMachine)

        verify {
            stateMachine.setUiState(withArg {
                assertIs<LoginViewState.Error>(it)
                assertEquals(DATA.username, it.username)
                assertEquals(DATA.password, it.password)
                assertEquals(ERROR.message, it.message)
                assertTrue { it.loginEnabled }
            })
        }
    }

    @Test
    fun processesUsernameChange() = test {
        val state = createState()
        state.start(stateMachine)

        state.process(LoginGesture.LoginChanged("newLogin"))

        verifyOrder {
            stateMachine.setUiState(withArg {
                assertIs<LoginViewState.Form>(it)
                assertEquals(DATA.username, it.username)
            })
            stateMachine.setUiState(withArg {
                assertIs<LoginViewState.Form>(it)
                assertEquals("newLogin", it.username)
            })
        }
    }

    @Test
    fun processesPasswordChange() = test {
        val state = createState()
        state.start(stateMachine)

        state.process(LoginGesture.PasswordChanged("newPassword"))

        verifyOrder {
            stateMachine.setUiState(withArg {
                assertIs<LoginViewState.Form>(it)
                assertEquals(DATA.password, it.password)
            })
            stateMachine.setUiState(withArg {
                assertIs<LoginViewState.Form>(it)
                assertEquals("newPassword", it.password)
            })
        }
    }

    @Test
    fun validatesInputData() = test {
        val state = createState()
        state.start(stateMachine)

        state.process(LoginGesture.LoginChanged("   "))

        verify {
            stateMachine.setUiState(withArg {
                assertIs<LoginViewState.Form>(it)
                assertEquals(DATA.username, it.username)
                assertTrue { it.loginEnabled }
            })
            stateMachine.setUiState(withArg {
                assertIs<LoginViewState.Form>(it)
                assertEquals("   ", it.username)
                assertFalse { it.loginEnabled }
            })
        }
    }

    @Test
    fun cancelsFlowOnBack() = test {
        every { factory.cancelled() } returns nextState

        val state = createState()
        state.start(stateMachine)

        state.process(LoginGesture.Back)

        verify {
            factory.cancelled()
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun startsLoginIfValid() = test {
        every { factory.loggingIn(any()) } returns nextState

        val state = createState()
        state.start(stateMachine)

        state.process(LoginGesture.LoginChanged("newLogin"))
        state.process(LoginGesture.PasswordChanged("newPassword"))
        state.process(LoginGesture.Login)

        verify {
            factory.loggingIn(withArg {
                assertEquals("newLogin", it.username)
                assertEquals("newPassword", it.password)
            })
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun blocksLoginIfFormIsInvalid() = test {
        val state = createState()
        state.start(stateMachine)

        state.process(LoginGesture.LoginChanged("  "))
        state.process(LoginGesture.Login)

        verify(exactly = 0) {
            factory.loggingIn(any())
            stateMachine.setMachineState(any())
        }
    }
    // endregion
}