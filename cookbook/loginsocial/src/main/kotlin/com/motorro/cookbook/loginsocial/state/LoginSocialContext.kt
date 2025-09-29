package com.motorro.cookbook.loginsocial.state

import com.motorro.cookbook.appcore.navigation.auth.AuthFlowHost

/**
 * Common login dependencies
 */
internal interface LoginSocialContext {
    /**
     * State factory
     */
    val factory: LoginSocialStateFactory

    /**
     * Flow host
     */
    val flowHost: AuthFlowHost
}