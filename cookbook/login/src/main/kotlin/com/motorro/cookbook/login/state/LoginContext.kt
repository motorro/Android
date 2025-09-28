package com.motorro.cookbook.login.state

import com.motorro.cookbook.appcore.navigation.auth.AuthFlowHost

/**
 * Common login dependencies
 */
internal interface LoginContext {
    /**
     * State factory
     */
    val factory: LoginStateFactory

    /**
     * Flow host
     */
    val flowHost: AuthFlowHost
}