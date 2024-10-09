package ru.merionet.tasks.login.state

import ru.merionet.tasks.login.data.LoginData
import ru.merionet.tasks.login.data.LoginGesture
import ru.merionet.tasks.login.data.LoginUiState
import kotlin.properties.Delegates

/**
 * Login form state
 * Handles user credentials input and validation
 */
class LoginFormState(context: LoginContext, data: LoginData) : BaseLoginState(context) {
    /**
     * Internal state data.
     * Updates every time the data changes
     */
    private var data: LoginData by Delegates.observable(data) { _, _, _ ->
        render()
    }

    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        super.doStart()
        render()
    }

    /**
     * Updates view with a new state
     */
    private fun render() = with(data) {
        setUiState(LoginUiState.Form(
            userName = userName,
            password = password,
            loginEnabled = isValid(),
            message = message
        ))
    }

    /**
     * Checks we have a valid login form
     */
    private fun isValid() = with(data) {
        (userName.isBlank() || password.isBlank()).not()
    }

    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: LoginGesture) {
        when(gesture) {
            LoginGesture.Action -> {
                d { "Action pressed" }
                if (isValid()) {
                    d { "Form is valid. Starting login procedure..." }
                    setMachineState(factory.loggingIn(data))
                }
            }
            LoginGesture.Back -> {
                d { "Back pressed. Terminating flow..." }
                setMachineState(factory.terminated())
            }
            is LoginGesture.PasswordChanged -> {
                data = data.copy(password = gesture.value)
            }
            is LoginGesture.UserNameChanged -> {
                data = data.copy(userName = gesture.value)
            }
            else -> super.doProcess(gesture)
        }
    }
}