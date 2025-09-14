package com.motorro.statemachine.statemachine

import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.commonstatemachine.ProxyMachineState
import com.motorro.statemachine.common.api.AuthFlowHost
import com.motorro.statemachine.login.LoginFlowApi
import com.motorro.statemachine.login.data.LoginFlowGesture
import com.motorro.statemachine.login.data.LoginFlowUiState
import com.motorro.statemachine.statemachine.data.AppGesture
import com.motorro.statemachine.statemachine.data.AppUiState
import com.motorro.statemachine.statemachine.data.LoginGesture
import com.motorro.statemachine.statemachine.data.LoginUiState

/**
 * Runs login flow
 */
class LoginProxy(factory: AppStateFactory) : ProxyMachineState<AppGesture, AppUiState, LoginFlowGesture, LoginFlowUiState>(LoginFlowUiState.Loading()) {

    private val flowHost = object : AuthFlowHost {
        override fun success(username: String) {
            setMachineState(factory.content())
        }

        override fun failure() {
            setMachineState(factory.terminated())
        }
    }

    override fun init(): CommonMachineState<LoginFlowGesture, LoginFlowUiState> {
        return LoginFlowApi.start(flowHost)
    }

    override fun mapGesture(parent: AppGesture): LoginFlowGesture? = when(parent) {
        AppGesture.Action -> LoginFlowGesture.Action
        AppGesture.Back -> LoginFlowGesture.Back
        is LoginGesture -> parent.child
        else -> null
    }

    override fun mapUiState(child: LoginFlowUiState): AppUiState {
        return LoginUiState(child)
    }
}