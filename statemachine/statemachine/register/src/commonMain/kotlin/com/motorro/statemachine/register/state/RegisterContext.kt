package com.motorro.statemachine.register.state

import com.motorro.statemachine.common.api.AuthFlowHost

/**
 * Common data passed to all login states
 */
internal interface RegisterContext {
    /**
     * Parent flow
     */
    val flowHost: AuthFlowHost

    /**
     * State factory
     */
    val factory: RegisterFlowStateFactory
}