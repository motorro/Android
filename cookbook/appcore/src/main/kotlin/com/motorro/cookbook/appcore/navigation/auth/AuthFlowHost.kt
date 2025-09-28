package com.motorro.cookbook.appcore.navigation.auth

import com.motorro.cookbook.model.Profile

/**
 * Authentication proxy state interface
 */
interface AuthFlowHost {
    /**
     * Called by child flow when it is finished
     */
    fun onLogin(profile: Profile)

    /**
     * Called by child flow when it is cancelled
     */
    fun onCancel()
}