package com.motorro.cookbook.login.state

/**
 * Common login dependencies
 */
internal interface LoginContext {
    /**
     * State factory
     */
    val factory: LoginStateFactory
}