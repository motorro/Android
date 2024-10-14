package ru.merionet.tasks.login.data

import ru.merionet.tasks.auth.data.SessionError

/**
 * Login view states - a sealed collection of all possible screen states found in Login flow.
 * Updates direction: Model -> UI
 */
sealed class LoginUiState {
    /**
     * Loading state
     */
    data object Loading : LoginUiState()

    /**
     * Login form
     * @property userName User name
     * @property password User password
     * @property loginEnabled True if login button is enabled
     * @property message Optional message for the user
     */
    data class Form(
        val userName: String,
        val password: String,
        val loginEnabled: Boolean,
        val message: String? = null
    ) : LoginUiState()

    /**
     * Login error
     * @property error Error to display
     */
    data class LoginError(val error: SessionError) : LoginUiState()
}