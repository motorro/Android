package com.motorro.statemachine.statemachine

import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.commonstatemachine.ProxyMachineState
import com.motorro.statemachine.auth.AuthFlowApi
import com.motorro.statemachine.auth.data.AuthFlowGesture
import com.motorro.statemachine.auth.data.AuthFlowUiState
import com.motorro.statemachine.common.api.AuthFlowHost
import com.motorro.statemachine.statemachine.data.AppGesture
import com.motorro.statemachine.statemachine.data.AppUiState
import com.motorro.statemachine.statemachine.data.AuthGesture
import com.motorro.statemachine.statemachine.data.AuthUiState

/**
 * Runs login flow
 */
class AuthProxy(factory: AppStateFactory) : ProxyMachineState<AppGesture, AppUiState, AuthFlowGesture, AuthFlowUiState>(AuthFlowApi.DEFAULT_UI_STATE) {

    private val flowHost = object : AuthFlowHost {
        override fun success(username: String) {
            setMachineState(factory.content())
        }

        override fun failure() {
            setMachineState(factory.terminated())
        }
    }

    override fun init(): CommonMachineState<AuthFlowGesture, AuthFlowUiState> {
        return AuthFlowApi.start(flowHost)
    }

    override fun mapGesture(parent: AppGesture): AuthFlowGesture? = when(parent) {
        AppGesture.Back -> AuthFlowGesture.Back
        is AuthGesture -> parent.child
        else -> null
    }

    override fun mapUiState(child: AuthFlowUiState): AppUiState {
        return AuthUiState(child)
    }
}