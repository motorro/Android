package com.motorro.statemachine.login.state

import com.motorro.statemachine.common.data.exception.AppException
import com.motorro.statemachine.login.data.LoginDataState
import com.motorro.statemachine.login.data.LoginFlowGesture
import com.motorro.statemachine.login.data.LoginFlowUiState

/**
 * Handles login error
 * @param context Login flow context
 * @param data Login data
 * @param error Login error
 */
internal class LoginErrorState(
    context: LoginContext,
    private val data: LoginDataState,
    private val error: AppException
) : BaseLoginState(context) {

    override fun doStart() {
        super.doStart()
        setUiState(LoginFlowUiState.Error(error.message, error.isFatal.not()))
    }

    override fun doProcess(gesture: LoginFlowGesture) {
        when(gesture) {
            is LoginFlowGesture.Action -> {
                info { "Action pressed..." }
                if (error.isFatal) {
                    info { "Fatal error. Returning to form..." }
                    setMachineState(factory.loginForm(data))
                } else {
                    info { "Non-fatal error. Retrying..." }
                    setMachineState(factory.loggingIn(data))
                }
            }
            LoginFlowGesture.Back -> {
                info { "Back pressed. Returning to form..." }
                setMachineState(factory.loginForm(data))
            }
            else -> super.doProcess(gesture)
        }
    }
}