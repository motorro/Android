package com.motorro.tasks.login.state

import com.motorro.tasks.login.data.LoginData

/**
 * Terminating state that updates the parent flow with cancellation
 */
class TerminatedState(
    context: LoginContext,
    private val data: LoginData
) : BaseLoginState(context) {
    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        super.doStart()
        d { "User did not log in" }
        data.flowHost.onNotAuthenticated()
    }
}