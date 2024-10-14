package ru.merionet.tasks.app.data

import ru.merionet.tasks.login.data.LoginGesture

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