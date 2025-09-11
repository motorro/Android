package com.motorro.statemachine.statemachine.login.data

/**
 * Internal data state to pass between state of login flow
 */
data class LoginDataState(val username: String = "", val password: String = "")