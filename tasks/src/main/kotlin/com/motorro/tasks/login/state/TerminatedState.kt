package com.motorro.tasks.login.state

import com.motorro.tasks.login.data.LoginUiState


class TerminatedState(context: LoginContext) : BaseLoginState(context) {
    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        super.doStart()
        setUiState(LoginUiState.Terminated)
    }
}