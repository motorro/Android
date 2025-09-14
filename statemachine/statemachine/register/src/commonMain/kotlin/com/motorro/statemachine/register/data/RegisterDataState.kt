package com.motorro.statemachine.register.data

/**
 * Internal data state to pass between state of registration flow
 */
internal data class RegisterDataState(
    val username: String = "",
    val password: String = "",
    val email: String = "",
    val eulaAccepted: Boolean = false
)