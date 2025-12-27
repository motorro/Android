package com.motorro.tasks.login.state

import com.motorro.tasks.login.data.LoginData

/**
 * Builds logic states for login flow
 */
interface LoginStateFactory {
    /**
     * Runs login flow
     * @param userName Known user-name (if any)
     * @param message Login message (if any)
     */
    fun init(userName: String? = null, message: String? = null): LoginState

    /**
     * Creates login form
     * @param data Login data so far
     */
    fun form(data: LoginData): LoginState

    /**
     * Running login operation
     * @param data Login data so far
     */
    fun loggingIn(data: LoginData): LoginState

    /**
     * Terminates the state flow
     * No updates after this state is set to state machine
     */
    fun terminated(): LoginState
}