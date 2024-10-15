package ru.merionet.tasks.login.state

import io.mockk.Ordering
import io.mockk.every
import io.mockk.verify
import org.junit.Test
import ru.merionet.tasks.USER_NAME
import ru.merionet.tasks.login.data.LoginGesture
import ru.merionet.tasks.login.data.LoginUiState
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class LoginFormStateTest : BaseStateTest() {
    private val state: LoginState = LoginFormState(context, loginData)

    @Test
    fun rendersFormOnStart() {
        state.start(stateMachine)

        verify {
            stateMachine.setUiState(LoginUiState.Form(
                userName = USER_NAME.value,
                password = "",
                loginEnabled = false,
                message = MESSAGE
            ))
        }
    }

    @Test
    fun updatesUserName() {
        val name = "User"
        state.start(stateMachine)
        state.process(LoginGesture.UserNameChanged(name))

        verify(ordering = Ordering.ORDERED) {
            stateMachine.setUiState(LoginUiState.Form(
                userName = USER_NAME.value,
                password = "",
                loginEnabled = false,
                message = MESSAGE
            ))
            stateMachine.setUiState(withArg {
                assertEquals(name, (it as LoginUiState.Form).userName)
            })
        }
    }

    @Test
    fun updatesPassword() {
        val password = "12345"
        state.start(stateMachine)
        state.process(LoginGesture.PasswordChanged(password))

        verify(ordering = Ordering.ORDERED) {
            stateMachine.setUiState(LoginUiState.Form(
                userName = USER_NAME.value,
                password = "",
                loginEnabled = false,
                message = MESSAGE
            ))
            stateMachine.setUiState(withArg {
                assertEquals(password, (it as LoginUiState.Form).password)
            })
        }
    }

    @Test
    fun whenFormIsInvalidDoesNotGoToLogin() {
        state.start(stateMachine)
        state.process(LoginGesture.Action)

        verify(ordering = Ordering.SEQUENCE) {
            stateMachine.setUiState(LoginUiState.Form(
                userName = USER_NAME.value,
                password = "",
                loginEnabled = false,
                message = MESSAGE
            ))
        }
    }

    @Test
    fun whenUserNameAndPasswordAreSetEnablesLogin() {
        val name = "User"
        state.start(stateMachine)
        state.process(LoginGesture.UserNameChanged(name))
        state.process(LoginGesture.PasswordChanged(PASSWORD))

        verify(ordering = Ordering.ORDERED) {
            stateMachine.setUiState(withArg {
                assertFalse { (it as LoginUiState.Form).loginEnabled }
            })
            stateMachine.setUiState(withArg {
                assertTrue { (it as LoginUiState.Form).loginEnabled }
            })
        }
    }

    @Test
    fun movesToLoginWhenValid() {
        every { factory.loggingIn(any()) } returns nextState

        val name = "User"
        val expectedData = loginData.copy(
            userName = name,
            password = PASSWORD
        )
        state.start(stateMachine)
        state.process(LoginGesture.UserNameChanged(name))
        state.process(LoginGesture.PasswordChanged(PASSWORD))
        state.process(LoginGesture.Action)

        verify(ordering = Ordering.ORDERED) {
            stateMachine.setUiState(withArg {
                assertFalse { (it as LoginUiState.Form).loginEnabled }
            })
            stateMachine.setUiState(withArg {
                assertTrue { (it as LoginUiState.Form).loginEnabled }
            })
            factory.loggingIn(expectedData)
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun terminatesOnBack() {
        every { factory.terminated(any()) } returns  nextState

        state.start(stateMachine)
        state.process(LoginGesture.Back)
        verify(ordering = Ordering.ORDERED) {
            factory.terminated(loginData)
            stateMachine.setMachineState(nextState)
        }
    }
}