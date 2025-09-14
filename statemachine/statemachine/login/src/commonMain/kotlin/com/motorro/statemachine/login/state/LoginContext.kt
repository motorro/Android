package com.motorro.statemachine.login.state

import com.motorro.statemachine.common.api.AuthFlowHost

/**
 * Common data passed to all login states
 */
internal interface LoginContext {
    /**
     * Parent flow
     */
    val flowHost: AuthFlowHost

    /**
     * State factory
     */
    val factory: LoginFlowStateFactory
}