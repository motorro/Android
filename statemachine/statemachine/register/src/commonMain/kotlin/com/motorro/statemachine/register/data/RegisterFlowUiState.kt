package com.motorro.statemachine.register.data

sealed class RegisterFlowUiState {
    /**
     * Loading state
     * @property message The loading message
     */
    data class Loading(val message: String? = null) : RegisterFlowUiState()

    /**
     * Error state
     * @property message The error message
     * @property canRetry Whether the error can be retried
     */
    data class Error(val message: String, val canRetry: Boolean = false) : RegisterFlowUiState()

    /**
     * Form state
     * @property username The username
     * @property password The password
     * @property email The email
     * @property nextEnabled Whether the next button is enabled
     */
    data class Form(val username: String, val password: String, val email: String, val nextEnabled: Boolean) : RegisterFlowUiState()

    /**
     * Eula state
     * @property eula The eula text
     * @property accepted Whether the eula is accepted
     * @property nextEnabled Whether the next button is enabled
     */
    data class Eula(val eula: String, val accepted: Boolean, val nextEnabled: Boolean) : RegisterFlowUiState()
}