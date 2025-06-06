package com.motorro.architecture.account.eu.data

/**
 * Social network UI state
 */
sealed class UiState {
    data object Prompt : UiState()
    data object LoggingIn : UiState()
    data object Complete : UiState()
}