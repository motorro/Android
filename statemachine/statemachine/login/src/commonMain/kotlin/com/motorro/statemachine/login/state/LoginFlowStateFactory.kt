package com.motorro.statemachine.login.state

import com.motorro.statemachine.common.api.AuthFlowHost
import com.motorro.statemachine.common.data.exception.AppException
import com.motorro.statemachine.login.data.LoginDataState

/**
 * Login flow state factory
 */
internal interface LoginFlowStateFactory {
    /**
     * Login form
     */
    fun loginForm(data: LoginDataState? = null): BaseLoginState

    /**
     * Running log-in
     */
    fun loggingIn(data: LoginDataState): BaseLoginState

    /**
     * Login error
     */
    fun loginError(data: LoginDataState, error: AppException): BaseLoginState
}

internal class LoginFlowStateFactoryImpl(flowHost: AuthFlowHost) : LoginFlowStateFactory {
    private val context: LoginContext = object : LoginContext {
        override val flowHost: AuthFlowHost = flowHost
        override val factory: LoginFlowStateFactory = this@LoginFlowStateFactoryImpl
    }

    override fun loginForm(data: LoginDataState?) = LoginFormState(
        context,
        data ?: LoginDataState()
    )

    override fun loggingIn(data: LoginDataState) = LoggingInState.Factory()(
        context,
        data
    )

    override fun loginError(data: LoginDataState, error: AppException) = LoginErrorState(
        context,
        data,
        error
    )
}