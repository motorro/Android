package com.motorro.statemachine.common.data.gesture

/**
 * Login flow gestures
 */
sealed class LoginGesture {
    /**
     * Default action
     */
    data object Action : LoginGesture()

    /**
     * Back-pressed
     */
    data object Back : LoginGesture()

    /**
     * Username changed
     * @property value The new username
     */
    data class UsernameChanged(val value: String) : LoginGesture()

    /**
     * Password changed
     * @property value The new password
     */
    data class PasswordChanged(val value: String) : LoginGesture()
}