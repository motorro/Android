package com.motorro.cookbook.login.data

/**
 * Login gesture
 */
sealed class LoginGesture {
    data object Back : LoginGesture()
    data object Login : LoginGesture()
    data class LoginChanged(val login: String) : LoginGesture()
    data class PasswordChanged(val password: String) : LoginGesture()
}