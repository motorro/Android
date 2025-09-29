package com.motorro.cookbook.loginsocial.state

import com.motorro.cookbook.appcore.navigation.auth.AuthFlowHost
import com.motorro.cookbook.core.error.CoreException
import com.motorro.cookbook.model.Profile
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import javax.inject.Provider

/**
 * Social login flow state factory
 */
internal interface LoginSocialStateFactory {

    /**
     * Flow start state
     */
    fun init(): LoginSocialState = buttons()

    /**
     * Social buttons
     */
    fun buttons(error: CoreException? = null): LoginSocialState

    /**
     * Runs login request
     */
    fun loggingIn(providerId: String): LoginSocialState

    /**
     * Login success
     */
    fun complete(profile: Profile): LoginSocialState

    /**
     * Login cancelled
     */
    fun cancelled(): LoginSocialState

    /**
     * Factory implementation
     */
    class Impl @AssistedInject constructor(
        private val createLoggingInState: Provider<LoggingInSocialState.Factory>,
        @Assisted flowHost: AuthFlowHost
    ) : LoginSocialStateFactory {

        private val context = object : LoginSocialContext {
            override val factory = this@Impl
            override val flowHost = flowHost
        }

        override fun buttons(error: CoreException?) = LoginSocialButtonsState(
            context,
            error
        )

        override fun loggingIn(providerId: String): LoginSocialState = createLoggingInState.get()(
            context,
            providerId
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