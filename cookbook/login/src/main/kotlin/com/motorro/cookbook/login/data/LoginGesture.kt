package com.motorro.cookbook.login.data

import com.motorro.cookbook.appcore.navigation.auth.AuthGesture

/**
 * Login gesture
 */
internal sealed class LoginGesture : AuthGesture{
    data object Back : LoginGesture()
    data object Login : LoginGesture()
    data class LoginChanged(val login: String) : LoginGesture()
    data class PasswordChanged(val password: String) : LoginGesture()
}