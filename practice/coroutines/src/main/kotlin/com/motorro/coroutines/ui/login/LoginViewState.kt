package com.motorro.coroutines.ui.login

import com.motorro.coroutines.ui.login.data.User

sealed class LoginViewState {
    data class Login(val error: Exception? = null) : LoginViewState()
    data object LoggingIn : LoginViewState()
    data class Content(val user: User) : LoginViewState()
    data object LoggingOut : LoginViewState()
}