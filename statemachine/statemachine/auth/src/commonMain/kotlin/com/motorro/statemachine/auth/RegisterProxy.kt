package com.motorro.statemachine.auth

import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.commonstatemachine.ProxyMachineState
import com.motorro.statemachine.auth.data.AuthFlowGesture
import com.motorro.statemachine.auth.data.AuthFlowUiState
import com.motorro.statemachine.common.api.AuthFlowHost
import com.motorro.statemachine.register.RegisterFlowApi
import com.motorro.statemachine.register.data.RegisterFlowGesture
import com.motorro.statemachine.register.data.RegisterFlowUiState

internal class RegisterProxy(flowHost: AuthFlowHost) : ProxyMachineState<AuthFlowGesture, AuthFlowUiState, RegisterFlowGesture, RegisterFlowUiState>(RegisterFlowUiState.Loading()) {
    private val flowHost = object : AuthFlowHost by flowHost {
        override fun failure() {
            setMachineState(AuthPromptState(flowHost))
        }
    }

    override fun init(): CommonMachineState<RegisterFlowGesture, RegisterFlowUiState> {
        return RegisterFlowApi.start(flowHost)
    }

    override fun mapGesture(parent: AuthFlowGesture): RegisterFlowGesture? = when(parent) {
        is AuthFlowGesture.Registration -> parent.child
        else -> null
    }

    override fun mapUiState(child: RegisterFlowUiState): AuthFlowUiState {
        return AuthFlowUiState.Registration(child)
    }
}