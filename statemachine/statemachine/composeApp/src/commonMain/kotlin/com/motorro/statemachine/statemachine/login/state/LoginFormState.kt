package com.motorro.statemachine.statemachine.login.state

import com.motorro.statemachine.statemachine.AppStateFactory
import com.motorro.statemachine.statemachine.BaseAppState
import com.motorro.statemachine.statemachine.data.AppGesture
import com.motorro.statemachine.statemachine.data.LoginGesture
import com.motorro.statemachine.statemachine.data.LoginUiState
import com.motorro.statemachine.statemachine.login.data.LoginDataState
import kotlin.properties.Delegates

/**
 * Manages login form state
 * @param factory State factory
 * @param data Login flow data
 */
class LoginFormState(factory: AppStateFactory, data: LoginDataState) : BaseAppState(factory) {

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
    override fun doProcess(gesture: AppGesture) {
        when(gesture) {
            is LoginGesture.UsernameChanged -> {
                data = data.copy(username = gesture.value)
            }
            is LoginGesture.PasswordChanged -> {
                data = data.copy(password = gesture.value)
            }
            is AppGesture.Action -> {
                if (data.isValid()) {
                    info { "Data is valid. Advancing to login..." }
                    setMachineState(factory.loggingIn(data))
                }
            }
            is AppGesture.Back -> {
                info { "Back pressed. Terminating..." }
                setMachineState(factory.terminated())
            }
            else -> super.doProcess(gesture)
        }
    }

    private fun LoginDataState.isValid(): Boolean = username.isNotEmpty() && password.isNotEmpty()

    private fun render(data: LoginDataState) {
        info { "Updating UI state..." }
        setUiState(
            LoginUiState.Form(
                username = data.username,
                password = data.password,
                loginEnabled = data.isValid()
            )
        )
    }
}