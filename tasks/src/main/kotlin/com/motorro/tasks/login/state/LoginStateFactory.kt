package com.motorro.tasks.login.state

import com.motorro.tasks.auth.data.SessionError
import com.motorro.tasks.data.SessionClaims
import com.motorro.tasks.login.LoginFlowHost
import com.motorro.tasks.login.data.LoginData
import javax.inject.Inject

/**
 * Builds logic states for login flow
 */
interface LoginStateFactory {
    /**
     * Runs login flow
     * @param host Login hosting flow to notify when done
     * @param userName Known user-name (if any)
     * @param message Login message (if any)
     */
    fun init(host: LoginFlowHost, userName: String? = null, message: String? = null): LoginState

    /**
     * Creates login form
     * @param data Login data
     */
    fun form(data: LoginData): LoginState

    /**
     * Running login operation
     * @param data Login data
     */
    fun loggingIn(data: LoginData): LoginState

    /**
     * Login error
     * @param data Login data
     * @param error Login error
     */
    fun loginError(data: LoginData, error: SessionError): LoginState

    /**
     * Login successful, active user session
     * @param claims Session claims
     */
    fun authenticated(data: LoginData, claims: SessionClaims): LoginState

    /**
     * Terminates the state flow
     * No updates after this state is set to state machine
     * @param data Login data
     */
    fun terminated(data: LoginData): LoginState

    /**
     * Factory implementation
     * States require various dependencies so it looks more readable if we use
     * intermediate factories
     */
    class Impl @Inject constructor(
        private val createLoggingIn: LoggingInState.Factory
    ): LoginStateFactory {

        private val context = object : LoginContext {
            override val factory: LoginStateFactory = this@Impl
        }

        override fun init(host: LoginFlowHost, userName: String?, message: String?) = form(LoginData(
            flowHost = host,
            userName = userName.orEmpty(),
            message = message
        ))

        override fun form(data: LoginData) = LoginFormState(
            context,
            data
        )

        override fun loggingIn(data: LoginData) = createLoggingIn(
            context,
            data
        )

        override fun loginError(data: LoginData, error: SessionError) = LoginError(
            context,
            data,
            error
        )

        override fun authenticated(data: LoginData, claims: SessionClaims) = AuthenticatedState(
            context,
            data,
            claims
        )

        override fun terminated(data: LoginData): LoginState = TerminatedState(
            context,
            data
        )
    }
}