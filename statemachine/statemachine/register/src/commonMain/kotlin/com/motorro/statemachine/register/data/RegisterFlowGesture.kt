package com.motorro.statemachine.register.data

sealed class RegisterFlowGesture {
    /**
     * Default action
     */
    data object Action : RegisterFlowGesture()

    /**
     * Back-pressed
     */
    data object Back : RegisterFlowGesture()

    /**
     * Username changed
     * @property value The new username
     */
    data class UsernameChanged(val value: String) : RegisterFlowGesture()

    /**
     * Password changed
     * @property value The new password
     */
    data class PasswordChanged(val value: String) : RegisterFlowGesture()

    /**
     * Email changed
     * @property value The new email
     */
    data class EmailChanged(val value: String) : RegisterFlowGesture()

    /**
     * Eula checkbox toggled
     */
    data object EulaToggled : RegisterFlowGesture()
}