package com.motorro.statemachine.common.data.ui

/**
 * Login screen state
 */
sealed class LoginUiState {
    /**
     * Loading state
     * @property state The common loading state
     */
    data class Loading(val state: LoadingUiState) : LoginUiState()

    /**
     * Error state
     * @property state The common error state
     */
    data class Error(val state: ErrorUiState) : LoginUiState()

    /**
     * Form state
     * @property username The username
     * @property password The password
     * @property loginEnabled Whether the login button is enabled
     */
    data class Form(val username: String, val password: String, val loginEnabled: Boolean) : LoginUiState()
}