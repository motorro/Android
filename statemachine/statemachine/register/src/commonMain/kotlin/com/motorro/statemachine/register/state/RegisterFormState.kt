package com.motorro.statemachine.register.state

import com.motorro.statemachine.register.data.RegisterDataState
import com.motorro.statemachine.register.data.RegisterFlowGesture
import com.motorro.statemachine.register.data.RegisterFlowUiState
import kotlin.properties.Delegates

/**
 * Manages registration form state
 * @param context Register flow context
 * @param data Register flow data
 */
internal class RegisterFormState(context: RegisterContext, data: RegisterDataState) : BaseRegisterState(context) {

    /**
     * Inner state data
     */
    private var data: RegisterDataState by Delegates.observable(data) { _, _, newValue ->
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
    override fun doProcess(gesture: RegisterFlowGesture) {
        when(gesture) {
            is RegisterFlowGesture.UsernameChanged -> {
                data = data.copy(username = gesture.value)
            }
            is RegisterFlowGesture.PasswordChanged -> {
                data = data.copy(password = gesture.value)
            }
            is RegisterFlowGesture.EmailChanged -> {
                data = data.copy(email = gesture.value)
            }
            is RegisterFlowGesture.Action -> {
                if (data.isValid()) {
                    info { "Data is valid. Advancing to EULA accept..." }
                    setMachineState(factory.eulaForm(data))
                }
            }
            is RegisterFlowGesture.Back -> {
                info { "Back pressed. Failing the flow..." }
                flowHost.failure()
            }
            else -> super.doProcess(gesture)
        }
    }

    private fun RegisterDataState.isValid(): Boolean = username.isNotEmpty() && password.isNotEmpty() && EMAIL_REGEX.matches(email)

    private fun render(data: RegisterDataState) {
        info { "Updating UI state..." }
        setUiState(
            RegisterFlowUiState.Form(
                username = data.username,
                password = data.password,
                email = data.email,
                nextEnabled = data.isValid()
            )
        )
    }

    private companion object {
        val EMAIL_REGEX = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})".toRegex()
    }
}