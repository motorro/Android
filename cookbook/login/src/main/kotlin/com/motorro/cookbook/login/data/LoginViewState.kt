package com.motorro.cookbook.login.data

/**
 * View-state for login form
 */
sealed class LoginViewState {

    abstract val controlsEnabled: Boolean
    abstract val loginEnabled: Boolean

    data class Form(val username: String, val password: String, override val loginEnabled: Boolean) : LoginViewState() {
        override val controlsEnabled: Boolean = true
    }
    data class Loading(val username: String, val password: String) : LoginViewState() {
        override val loginEnabled: Boolean = false
        override val controlsEnabled: Boolean = false
    }
    data class Error(val message: String, val username: String, val password: String, override val loginEnabled: Boolean) : LoginViewState() {
        override val controlsEnabled: Boolean = true
    }
    data object LoggedIn : LoginViewState() {
        override val loginEnabled: Boolean = false
        override val controlsEnabled: Boolean = false
    }
}