package ru.merionet.tasks.login.state

import ru.merionet.tasks.auth.SessionManager
import javax.inject.Inject

/**
 * Starting state for the main flow
 * Checks auth and if there is already - launches the main flow
 * Otherwise launches the login flow
 */
class CheckingAuthState(
    context: LoginContext,
    private val sessionManager: SessionManager
) : BaseLoginState(context) {

    /**
     * State factory to inject all the external dependencies and keep the main factory
     * clean
     */
    class Factory @Inject constructor(private val sessionManager: SessionManager) {
        operator fun invoke(context: LoginContext) = CheckingAuthState(
            context,
            sessionManager
        )
    }
}