package com.motorro.statemachine.login.data

sealed class LoginFlowUiState {
    /**
     * Loading state
     * @property message The loading message
     */
    data class Loading(val message: String? = null) : LoginFlowUiState()

    /**
     * Error state
     * @property message The error message
     * @property canRetry Whether the error can be retried
     */
    data class Error(val message: String, val canRetry: Boolean = false) : LoginFlowUiState()

    /**
     * Form state
     * @property username The username
     * @property password The password
     * @property loginEnabled Whether the login button is enabled
     */
    data class Form(val username: String, val password: String, val loginEnabled: Boolean) : LoginFlowUiState()
}