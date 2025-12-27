package com.motorro.tasks.login.state

import com.motorro.tasks.data.SessionClaims
import com.motorro.tasks.domain.data.User
import com.motorro.tasks.login.data.LoginData

/**
 * Terminating state that updates the parent flow with logged-in user
 */
class AuthenticatedState(
    context: LoginContext,
    private val data: LoginData,
    private val claims: SessionClaims
) : BaseLoginState(context) {
    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        super.doStart()
        d { "User ${claims.username} logged in" }
        data.flowHost.onAuthenticated(User(claims.username))
    }
}