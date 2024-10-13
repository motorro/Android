package ru.merionet.tasks.login.state

import ru.merionet.tasks.login.data.LoginData
import javax.inject.Inject

/**
 * Builds logic states for login flow
 */
interface LoginStateFactory {
    /**
     * Runs login flow
     * @param userName Known user-name (if any)
     * @param message Login message (if any)
     */
    fun init(userName: String? = null, message: String? = null): LoginState

    /**
     * Creates login form
     * @param data Login data so far
     */
    fun form(data: LoginData): LoginState

    /**
     * Running login operation
     * @param data Login data so far
     */
    fun loggingIn(data: LoginData): LoginState

    /**
     * Terminates the state flow
     * No updates after this state is set to state machine
     */
    fun terminated(): LoginState

    /**
     * Factory implementation
     * States require various dependencies so it looks more readable if we use
     * intermediate factories
     */
    class Impl @Inject constructor(
        private val createCheckingAuthState: CheckingAuthState.Factory
    ): LoginStateFactory {

        private val context = object : LoginContext {
            override val factory: LoginStateFactory = this@Impl
        }

        override fun init(userName: String?, message: String?) = form(LoginData(
            userName = userName.orEmpty(),
            message = message
        ))

        override fun form(data: LoginData) = LoginFormState(
            context,
            data
        )

        override fun loggingIn(data: LoginData) = LoggingInState(
            context,
            data
        )

        override fun terminated() = TerminatedState(context)
    }
}