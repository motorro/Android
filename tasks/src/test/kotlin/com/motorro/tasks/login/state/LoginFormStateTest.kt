package com.motorro.tasks.login.state

import com.motorro.tasks.login.data.LoginGesture
import com.motorro.tasks.login.data.LoginUiState
import io.mockk.every
import io.mockk.verify
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class LoginFormStateTest : BaseStateTest() {
    private val state: LoginState = LoginFormState(context, loginData)

    @Test
    fun rendersFormOnStart() {
        state.start(stateMachine)

        verify {
            stateMachine.setUiState(
                LoginUiState.Form(
                userName = USER_NAME,
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

        val updates = mutableListOf<LoginUiState>()
        verify { stateMachine.setUiState(capture(updates)) }

        assertEquals(
            LoginUiState.Form(
                userName = USER_NAME,
                password = "",
                loginEnabled = false,
                message = MESSAGE
            ),
            updates.first()
        )

        assertEquals(
            name,
            (updates.last() as LoginUiState.Form).userName
        )
    }

    @Test
    fun updatesPassword() {
        val password = "12345"
        state.start(stateMachine)
        state.process(LoginGesture.PasswordChanged(password))

        val updates = mutableListOf<LoginUiState>()
        verify { stateMachine.setUiState(capture(updates)) }

        assertEquals(
            LoginUiState.Form(
                userName = USER_NAME,
                password = "",
                loginEnabled = false,
                message = MESSAGE
            ),
            updates.first()
        )
        assertEquals(
            password,
            (updates.last() as LoginUiState.Form).password
        )
    }

    @Test
    fun whenFormIsInvalidDoesNotGoToLogin() {
        state.start(stateMachine)
        state.process(LoginGesture.Action)

        verify {
            stateMachine.setUiState(
                LoginUiState.Form(
                    userName = USER_NAME,
                    password = "",
                    loginEnabled = false,
                    message = MESSAGE
                )
            )
        }
    }

    @Test
    fun whenUserNameAndPasswordAreSetEnablesLogin() {
        val name = "User"
        state.start(stateMachine)
        state.process(LoginGesture.UserNameChanged(name))
        state.process(LoginGesture.PasswordChanged(PASSWORD))

        val updates = mutableListOf<LoginUiState>()
        verify { stateMachine.setUiState(capture(updates)) }

        assertFalse {
            (updates.first() as LoginUiState.Form).loginEnabled
        }
        assertTrue {
            (updates.last() as LoginUiState.Form).loginEnabled
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

        verify {
            factory.loggingIn(expectedData)
            stateMachine.setMachineState(nextState)
        }
    }

    @Test
    fun terminatesOnBack() {
        every { factory.terminated() } returns  nextState

        state.start(stateMachine)
        state.process(LoginGesture.Back)
        verify {
            factory.terminated()
            stateMachine.setMachineState(nextState)
        }
    }
}