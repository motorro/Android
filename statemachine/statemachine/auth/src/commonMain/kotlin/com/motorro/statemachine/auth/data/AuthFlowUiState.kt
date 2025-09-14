package com.motorro.statemachine.auth.data

import com.motorro.statemachine.login.data.LoginFlowUiState
import com.motorro.statemachine.register.data.RegisterFlowUiState

sealed class AuthFlowUiState {
    /**
     * Authentication prompt
     */
    data object Prompt : AuthFlowUiState()

    /**
     * Registration flow
     */
    data class Registration(val child: RegisterFlowUiState) : AuthFlowUiState()

    /**
     * Login flow
     */
    data class Login(val child: LoginFlowUiState) : AuthFlowUiState()
}