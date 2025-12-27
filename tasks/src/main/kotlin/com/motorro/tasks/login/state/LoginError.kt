package com.motorro.tasks.login.state

import com.motorro.tasks.auth.data.SessionError
import com.motorro.tasks.login.data.LoginData
import com.motorro.tasks.login.data.LoginGesture
import com.motorro.tasks.login.data.LoginUiState

/**
 * Login error state
 */
class LoginError(
    context: LoginContext,
    private val data: LoginData,
    private val error: SessionError
) : BaseLoginState(context) {
    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        super.doStart()
        setUiState(LoginUiState.LoginError(error))
    }

    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: LoginGesture) {
        when(gesture) {
            LoginGesture.Action -> {
                if (error.retriable) {
                    d { "Retrying login error" }
                    setMachineState(factory.loggingIn(data))
                } else {
                    d { "Dismissing fatal login error. Returning to login form..." }
                    setMachineState(factory.form(data))
                }
            }
            LoginGesture.Back -> {
                d { "Back pressed. Returning to login form..." }
                setMachineState(factory.form(data))
            }
            else -> super.doProcess(gesture)
        }
    }
}