package com.motorro.cookbook.login.state

import com.motorro.cookbook.appcore.navigation.auth.AuthFlowHost
import com.motorro.cookbook.core.error.CoreException
import com.motorro.cookbook.login.data.LoginFlowData
import com.motorro.cookbook.model.Profile
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import javax.inject.Provider

/**
 * Login flow state factory
 */
internal interface LoginStateFactory {

    /**
     * Flow start state
     */
    fun init(): LoginState = form(LoginFlowData())

    /**
     * Data entry form
     */
    fun form(data: LoginFlowData, error: CoreException? = null): LoginState

    /**
     * Runs login request
     */
    fun loggingIn(data: LoginFlowData): LoginState

    /**
     * Login success
     */
    fun complete(profile: Profile): LoginState

    /**
     * Login cancelled
     */
    fun cancelled(): LoginState

    /**
     * Factory implementation
     */
    class Impl @AssistedInject constructor(
        private val createLoggingInState: Provider<LoggingInState.Factory>,
        @Assisted flowHost: AuthFlowHost
    ) : LoginStateFactory {

        private val context = object : LoginContext {
            override val factory = this@Impl
            override val flowHost = flowHost
        }

        override fun form(data: LoginFlowData, error: CoreException?) = LoginFormState(
            context,
            data,
            error
        )

        override fun loggingIn(data: LoginFlowData) = createLoggingInState.get()(
            context,
            data
        )

        override fun complete(profile: Profile) = TerminatedState(
            context,
            profile
        )

        override fun cancelled() = TerminatedState(
            context
        )

        @AssistedFactory
        interface Factory {
            fun create(flowHost: AuthFlowHost): Impl
        }
    }
}