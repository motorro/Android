package com.motorro.statemachine.common.data.ui

/**
 * Registration screen data
 */
sealed class RegistrationUiState {
    /**
     * Loading state
     * @property state The common loading state
     */
    data class Loading(val state: LoadingUiState) : RegistrationUiState()

    /**
     * Error state
     * @property state The common error state
     */
    data class Error(val state: ErrorUiState) : RegistrationUiState()

    /**
     * Form state
     * @property username The username
     * @property password The password
     * @property email The email
     * @property nextEnabled Whether the next button is enabled
     */
    data class Form(val username: String, val password: String, val email: String, val nextEnabled: Boolean) : RegistrationUiState()

    /**
     * Eula state
     * @property eula The eula text
     * @property accepted Whether the eula is accepted
     * @property nextEnabled Whether the next button is enabled
     */
    data class Eula(val eula: String, val accepted: Boolean, val nextEnabled: Boolean) : RegistrationUiState()
}