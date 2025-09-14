package com.motorro.statemachine.login.state

import com.motorro.statemachine.login.data.LoginDataState
import com.motorro.statemachine.login.data.LoginFlowGesture
import com.motorro.statemachine.login.data.LoginFlowUiState
import kotlin.properties.Delegates

/**
 * Manages login form state
 * @param context Login flow context
 * @param data Login flow data
 */
internal class LoginFormState(context: LoginContext, data: LoginDataState) : BaseLoginState(context) {

    /**
     * Inner state data
     */
    private var data: LoginDataState by Delegates.observable(data) { _, _, newValue ->
        info { "Data changed: $newValue" }
        render(newValue)
    }

    /**
     * Called when the state is started
     */
    override fun doStart() {
        super.doStart()
        render(data)
    }

    /**
     * Called to process the gesture
     */
    override fun doProcess(gesture: LoginFlowGesture) {
        when(gesture) {
            is LoginFlowGesture.UsernameChanged -> {
                data = data.copy(username = gesture.value)
            }
            is LoginFlowGesture.PasswordChanged -> {
                data = data.copy(password = gesture.value)
            }
            is LoginFlowGesture.Action -> {
                if (data.isValid()) {
                    info { "Data is valid. Advancing to login..." }
                    setMachineState(factory.loggingIn(data))
                }
            }
            is LoginFlowGesture.Back -> {
                info { "Back pressed. Failing the flow..." }
                flowHost.failure()
            }
        }
    }

    private fun LoginDataState.isValid(): Boolean = username.isNotEmpty() && password.isNotEmpty()

    private fun render(data: LoginDataState) {
        info { "Updating UI state..." }
        setUiState(
            LoginFlowUiState.Form(
                username = data.username,
                password = data.password,
                loginEnabled = data.isValid()
            )
        )
    }
}