package com.motorro.statemachine.statemachine.data

import com.motorro.statemachine.auth.data.AuthFlowUiState

/**
 * Auth flow UI state
 */
data class AuthUiState(val child: AuthFlowUiState) : AppUiState