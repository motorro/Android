package com.motorro.statemachine.statemachine.data

/**
 * Application UI state
 */
sealed interface AppUiState {
    /**
     * Common loading
     */
    data class Loading(val message: String? = null) : AppUiState

    /**
     * Terminated
     */
    data object Terminated : AppUiState
}