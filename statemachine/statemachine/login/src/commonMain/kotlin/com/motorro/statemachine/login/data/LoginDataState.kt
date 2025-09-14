package com.motorro.statemachine.login.data

/**
 * Internal data state to pass between state of login flow
 */
internal data class LoginDataState(val username: String = "", val password: String = "")