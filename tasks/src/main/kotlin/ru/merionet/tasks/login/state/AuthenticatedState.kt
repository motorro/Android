package ru.merionet.tasks.login.state

import ru.merionet.tasks.data.SessionClaims
import ru.merionet.tasks.domain.data.User
import ru.merionet.tasks.login.data.LoginData

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