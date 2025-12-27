package com.motorro.tasks.app.data

import com.motorro.tasks.login.data.LoginGesture

/**
 * Application gesture
 */
sealed class AppGesture {
    /**
     * Back pressed
     */
    data object Back : AppGesture()

    /**
     * Default action
     */
    data object Action : AppGesture()

    /**
     * Login flow gesture
     */
    data class Login(val child: LoginGesture) : AppGesture()

    /**
     * Logout
     */
    data object Logout : AppGesture()
}