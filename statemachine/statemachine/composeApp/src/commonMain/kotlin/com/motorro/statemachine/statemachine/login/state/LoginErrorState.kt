package com.motorro.statemachine.statemachine.login.state

import com.motorro.statemachine.common.data.exception.AppException
import com.motorro.statemachine.statemachine.AppStateFactory
import com.motorro.statemachine.statemachine.BaseAppState
import com.motorro.statemachine.statemachine.data.AppGesture
import com.motorro.statemachine.statemachine.data.LoginUiState
import com.motorro.statemachine.statemachine.login.data.LoginDataState

/**
 * Handles login error
 * @param factory State factory
 * @param data Login data
 * @param error Login error
 */
class LoginErrorState(
    factory: AppStateFactory,
    private val data: LoginDataState,
    private val error: AppException
) : BaseAppState(factory) {

    override fun doStart() {
        super.doStart()
        setUiState(LoginUiState.Error(error.message, error.isFatal.not()))
    }

    override fun doProcess(gesture: AppGesture) {
        when(gesture) {
            is AppGesture.Action -> {
                info { "Action pressed..." }
                if (error.isFatal) {
                    info { "Fatal error. Returning to form..." }
                    setMachineState(factory.loginForm(data))
                } else {
                    info { "Non-fatal error. Retrying..." }
                    setMachineState(factory.loggingIn(data))
                }
            }
            AppGesture.Back -> {
                info { "Back pressed. Returning to form..." }
                setMachineState(factory.loginForm(data))
            }
            else -> super.doProcess(gesture)
        }
    }
}