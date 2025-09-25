package com.motorro.cookbook.login.state

import com.motorro.cookbook.core.error.CoreException
import com.motorro.cookbook.login.data.LoginFlowData
import com.motorro.cookbook.model.Profile

/**
 * Login flow state factory
 */
internal interface LoginStateFactory {
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
}