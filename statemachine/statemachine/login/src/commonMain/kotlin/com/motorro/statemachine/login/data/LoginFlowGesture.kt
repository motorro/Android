package com.motorro.statemachine.login.data

sealed class LoginFlowGesture {
    /**
     * Default action
     */
    data object Action : LoginFlowGesture()

    /**
     * Back-pressed
     */
    data object Back : LoginFlowGesture()

    /**
     * Username changed
     * @property value The new username
     */
    data class UsernameChanged(val value: String) : LoginFlowGesture()

    /**
     * Password changed
     * @property value The new password
     */
    data class PasswordChanged(val value: String) : LoginFlowGesture()
}