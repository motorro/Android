package com.motorro.statemachine.common.api

/**
 * Interface for the object that runs a particular authentication flow.
 */
interface AuthFlowHost {
    /**
     * Called when the user has successfully authenticated.
     */
    fun success(username: String)

    /**
     * Called when the user has failed to authenticate.
     */
    fun failure()
}