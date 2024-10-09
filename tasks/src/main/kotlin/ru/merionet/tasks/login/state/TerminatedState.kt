package ru.merionet.tasks.login.state

import ru.merionet.tasks.login.data.LoginUiState

class TerminatedState(context: LoginContext) : BaseLoginState(context) {
    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        super.doStart()
        setUiState(LoginUiState.Terminated)
    }
}