package com.motorro.statemachine.statemachine.data

/**
 * Login state gesture
 */
interface LoginGesture : AppGesture {
    /**
     * Username changed
     * @property value The new username
     */
    data class UsernameChanged(val value: String) : AppGesture

    /**
     * Password changed
     * @property value The new password
     */
    data class PasswordChanged(val value: String) : AppGesture
}