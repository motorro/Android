package com.motorro.statemachine.register.state

import com.motorro.statemachine.common.data.exception.AppException
import com.motorro.statemachine.register.data.RegisterDataState
import com.motorro.statemachine.register.data.RegisterFlowGesture
import com.motorro.statemachine.register.data.RegisterFlowUiState

/**
 * Handles registration error
 * @param context Register flow context
 * @param data Register data
 * @param error Register error
 */
internal class RegisterErrorState(
    context: RegisterContext,
    private val data: RegisterDataState,
    private val error: AppException
) : BaseRegisterState(context) {

    override fun doStart() {
        super.doStart()
        setUiState(RegisterFlowUiState.Error(error.message, error.isFatal.not()))
    }

    override fun doProcess(gesture: RegisterFlowGesture) {
        when(gesture) {
            is RegisterFlowGesture.Action -> {
                info { "Action pressed..." }
                if (error.isFatal) {
                    info { "Fatal error. Returning to form..." }
                    setMachineState(factory.dataForm(data))
                } else {
                    info { "Non-fatal error. Retrying..." }
                    setMachineState(factory.registering(data))
                }
            }
            RegisterFlowGesture.Back -> {
                info { "Back pressed. Returning to form..." }
                setMachineState(factory.dataForm(data))
            }
            else -> super.doProcess(gesture)
        }
    }
}