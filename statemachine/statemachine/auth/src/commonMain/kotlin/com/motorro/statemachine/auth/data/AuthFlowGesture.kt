package com.motorro.statemachine.auth.data

import com.motorro.statemachine.login.data.LoginFlowGesture
import com.motorro.statemachine.register.data.RegisterFlowGesture

sealed class AuthFlowGesture {
    /**
     * Back-pressed
     */
    data object Back : AuthFlowGesture()

    /**
     * Register
     */
    data object RegisterPressed : AuthFlowGesture()

    /**
     * Register
     */
    data class Registration(val child: RegisterFlowGesture) : AuthFlowGesture()

    /**
     * Login
     */
    data object LoginPressed : AuthFlowGesture()

    /**
     * Register
     */
    data class Login(val child: LoginFlowGesture) : AuthFlowGesture()
}