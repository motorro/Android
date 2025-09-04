package com.motorro.statemachine.common.data.gesture

/**
 * Login flow gestures
 */
sealed class RegistrationGesture {
    /**
     * Default action
     */
    data object Action : RegistrationGesture()

    /**
     * Back-pressed
     */
    data object Back : RegistrationGesture()

    /**
     * Username changed
     * @property value The new username
     */
    data class UsernameChanged(val value: String) : RegistrationGesture()

    /**
     * Password changed
     * @property value The new password
     */
    data class PasswordChanged(val value: String) : RegistrationGesture()

    /**
     * Email changed
     * @property value The new email
     */
    data class EmailChanged(val value: String) : RegistrationGesture()

    /**
     * Eula checkbox toggled
     */
    data object EulaToggled : RegistrationGesture()
}