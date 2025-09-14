package com.motorro.statemachine.register.state

import com.motorro.statemachine.register.data.RegisterDataState
import com.motorro.statemachine.register.data.RegisterFlowGesture
import com.motorro.statemachine.register.data.RegisterFlowUiState
import kotlin.properties.Delegates

/**
 * Manages registration EULA state
 * @param context Register flow context
 * @param data Register flow data
 */
internal class RegisterEulaState(context: RegisterContext, data: RegisterDataState) : BaseRegisterState(context) {

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
            is RegisterFlowGesture.EulaToggled -> {
                data = data.copy(eulaAccepted = data.eulaAccepted.not())
            }
            is RegisterFlowGesture.Action -> {
                if (data.eulaAccepted) {
                    info { "EULA accepted. Advancing to registration..." }
                    setMachineState(factory.registering(data))
                }
            }
            is RegisterFlowGesture.Back -> {
                info { "Back pressed. Returning to data form..." }
                setMachineState(factory.dataForm(data))
            }
            else -> super.doProcess(gesture)
        }
    }

    private fun render(data: RegisterDataState) {
        info { "Updating UI state..." }
        setUiState(
            RegisterFlowUiState.Eula(
                eula = "Please accept the EULA to continue",
                accepted = data.eulaAccepted,
                nextEnabled = data.eulaAccepted
            )
        )
    }
}