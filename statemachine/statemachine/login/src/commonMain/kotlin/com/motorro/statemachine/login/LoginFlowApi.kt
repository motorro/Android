package com.motorro.statemachine.login

import androidx.compose.runtime.Composable
import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.statemachine.common.api.AuthFlowHost
import com.motorro.statemachine.login.data.LoginFlowGesture
import com.motorro.statemachine.login.data.LoginFlowUiState
import com.motorro.statemachine.login.state.LoginFlowStateFactoryImpl
import com.motorro.statemachine.login.ui.Error
import com.motorro.statemachine.login.ui.Form
import com.motorro.statemachine.login.ui.LoggingIn

/**
 * Module API
 */
interface LoginFlowApi {
    /**
     * Starts login flow
     */
    fun start(flowHost: AuthFlowHost): CommonMachineState<LoginFlowGesture, LoginFlowUiState>

    companion object : LoginFlowApi {
        override fun start(flowHost: AuthFlowHost): CommonMachineState<LoginFlowGesture, LoginFlowUiState> {
            return LoginFlowStateFactoryImpl(flowHost).loginForm()
        }
    }
}

/**
 * Module view
 */
@Composable
fun LoginScreen(state: LoginFlowUiState, onGesture: (LoginFlowGesture) -> Unit) {
    when(state) {
        is LoginFlowUiState.Form -> Form(state, onGesture)
        is LoginFlowUiState.Loading -> LoggingIn(state.message, onGesture)
        is LoginFlowUiState.Error -> Error(state, onGesture)
    }
}
