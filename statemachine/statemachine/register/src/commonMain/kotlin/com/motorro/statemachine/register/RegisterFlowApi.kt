package com.motorro.statemachine.register

import androidx.compose.runtime.Composable
import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.statemachine.common.api.AuthFlowHost
import com.motorro.statemachine.register.data.RegisterFlowGesture
import com.motorro.statemachine.register.data.RegisterFlowUiState
import com.motorro.statemachine.register.state.RegisterFlowStateFactoryImpl
import com.motorro.statemachine.register.ui.Error
import com.motorro.statemachine.register.ui.Eula
import com.motorro.statemachine.register.ui.Form
import com.motorro.statemachine.register.ui.Registering

/**
 * Module API
 */
interface RegisterFlowApi {
    /**
     * Starts login flow
     */
    fun start(flowHost: AuthFlowHost): CommonMachineState<RegisterFlowGesture, RegisterFlowUiState>

    companion object Companion : RegisterFlowApi {
        override fun start(flowHost: AuthFlowHost): CommonMachineState<RegisterFlowGesture, RegisterFlowUiState> {
            return RegisterFlowStateFactoryImpl(flowHost).dataForm()
        }
    }
}

/**
 * Module view
 */
@Composable
fun RegisterScreen(state: RegisterFlowUiState, onGesture: (RegisterFlowGesture) -> Unit) {
    when(state) {
        is RegisterFlowUiState.Form -> Form(state, onGesture)
        is RegisterFlowUiState.Loading -> Registering(state.message, onGesture)
        is RegisterFlowUiState.Eula -> Eula(state, onGesture)
        is RegisterFlowUiState.Error -> Error(state, onGesture)
    }
}

