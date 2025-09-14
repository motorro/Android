package com.motorro.statemachine.auth

import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.commonstatemachine.ProxyMachineState
import com.motorro.statemachine.auth.data.AuthFlowGesture
import com.motorro.statemachine.auth.data.AuthFlowUiState
import com.motorro.statemachine.common.api.AuthFlowHost
import com.motorro.statemachine.login.LoginFlowApi
import com.motorro.statemachine.login.data.LoginFlowGesture
import com.motorro.statemachine.login.data.LoginFlowUiState

internal class LoginProxy(flowHost: AuthFlowHost) : ProxyMachineState<AuthFlowGesture, AuthFlowUiState, LoginFlowGesture, LoginFlowUiState>(LoginFlowUiState.Loading()) {
    private val flowHost = object : AuthFlowHost by flowHost {
        override fun failure() {
            setMachineState(AuthPromptState(flowHost))
        }
    }

    override fun init(): CommonMachineState<LoginFlowGesture, LoginFlowUiState> {
        return LoginFlowApi.start(flowHost)
    }

    override fun mapGesture(parent: AuthFlowGesture): LoginFlowGesture? = when(parent) {
        is AuthFlowGesture.Login -> parent.child
        else -> null
    }

    override fun mapUiState(child: LoginFlowUiState): AuthFlowUiState {
        return AuthFlowUiState.Login(child)
    }
}