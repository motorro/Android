package com.motorro.cookbook.login.data

/**
 * View-state for login form
 */
sealed class LoginViewState {

    abstract val controlsEnabled: Boolean
    abstract val loginEnabled: Boolean

    abstract val username: String
    abstract val password: String

    data class Form(override val username: String, override val password: String, override val loginEnabled: Boolean) : LoginViewState() {
        override val controlsEnabled: Boolean = true
    }
    data class Loading(override val username: String, override val password: String) : LoginViewState() {
        override val loginEnabled: Boolean = false
        override val controlsEnabled: Boolean = false
    }
    data class Error(val message: String, override val username: String, override val password: String, override val loginEnabled: Boolean) : LoginViewState() {
        override val controlsEnabled: Boolean = true
    }
    data object LoggedIn : LoginViewState() {
        override val loginEnabled: Boolean = false
        override val controlsEnabled: Boolean = false
        override val username: String = ""
        override val password: String = ""
    }

    companion object {
        /**
         * Empty state
         */
        val EMPTY = Form("", "", false)
    }
}