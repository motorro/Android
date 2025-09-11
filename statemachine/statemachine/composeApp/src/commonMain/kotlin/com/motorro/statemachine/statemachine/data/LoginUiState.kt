package com.motorro.statemachine.statemachine.data

/**
 * Login flow UI state
 */
sealed interface LoginUiState : AppUiState {
    /**
     * Login form state
     * @property username The username
     * @property password The password
     * @property loginEnabled Whether the login button is enabled
     */
    data class Form(val username: String, val password: String, val loginEnabled: Boolean) : LoginUiState

    /**
     * Error state
     * @property message The error message
     * @property canRetry Whether the action can be retried
     */
    data class Error(val message: String, val canRetry: Boolean) : LoginUiState
}