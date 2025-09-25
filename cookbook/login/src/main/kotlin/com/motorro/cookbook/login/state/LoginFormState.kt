package com.motorro.cookbook.login.state

import androidx.annotation.VisibleForTesting
import com.motorro.cookbook.core.error.CoreException
import com.motorro.cookbook.login.data.LoginFlowData
import com.motorro.cookbook.login.data.LoginGesture
import com.motorro.cookbook.login.data.LoginViewState
import kotlin.properties.Delegates

/**
 * Manages login form
 */
internal class LoginFormState(
    context: LoginContext,
    data: LoginFlowData,
    private val error: CoreException? = null
) : LoginState(context) {

    private var data: LoginFlowData by Delegates.observable(data) { _, _, new ->
        render(new)
    }

    override fun doStart() {
        super.doStart()
        render(data)
    }

    override fun doProcess(gesture: LoginGesture) {
        when(gesture) {
            LoginGesture.Back -> {
                d { "Back gesture. Terminating..." }
                setMachineState(factory.cancelled())
            }
            LoginGesture.Login -> {
                if (isValid(data)) {
                    d { "Valid data. Transferring to login..." }
                    setMachineState(factory.loggingIn(data))
                }
            }
            is LoginGesture.LoginChanged -> {
                data = data.copy(username = gesture.login)
            }
            is LoginGesture.PasswordChanged -> {
                data = data.copy(password = gesture.password)
            }
        }
    }

    private fun render(data: LoginFlowData) {
        if (null != error) {
            setUiState(LoginViewState.Error(
                error.message,
                data.username,
                data.password,
                isValid(data)
            ))
        } else {
            setUiState(LoginViewState.Form(
                data.username,
                data.password,
                isValid(data)
            ))
        }
    }

    companion object {
        @VisibleForTesting
        fun isValid(data: LoginFlowData): Boolean = data.username.isNotBlank() && data.password.isNotBlank()
    }
}