package com.motorro.statemachine.auth

import com.motorro.statemachine.auth.data.AuthFlowGesture
import com.motorro.statemachine.auth.data.AuthFlowUiState
import com.motorro.statemachine.common.api.AuthFlowHost
import com.motorro.statemachine.common.state.BaseCoroutineState

internal class AuthPromptState(private val flowHost: AuthFlowHost) : BaseCoroutineState<AuthFlowGesture, AuthFlowUiState>() {
    override fun doStart() {
        setUiState(AuthFlowUiState.Prompt)
    }

    override fun doProcess(gesture: AuthFlowGesture) {
        when(gesture) {
            AuthFlowGesture.Back -> {
                info { "Back pressed. Aborting authentication flow..." }
                flowHost.failure()
            }
            AuthFlowGesture.LoginPressed -> {
                info { "Login pressed. Starting login flow..." }
                setMachineState(LoginProxy(flowHost))
            }
            AuthFlowGesture.RegisterPressed -> {
                info { "Register pressed. Starting register flow..." }
                setMachineState(RegisterProxy(flowHost))
            }
            else -> super.doProcess(gesture)
        }
    }
}