package com.motorro.statemachine.statemachine.data

import com.motorro.statemachine.login.data.LoginFlowUiState

/**
 * Login flow UI state
 */
data class LoginUiState(val child: LoginFlowUiState) : AppUiState