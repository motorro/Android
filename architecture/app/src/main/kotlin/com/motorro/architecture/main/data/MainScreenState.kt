package com.motorro.architecture.main.data

/**
 * Main screen state
 */
sealed class MainScreenState {
    data object Authenticating : MainScreenState()
    data object Registering : MainScreenState()
    data object Content : MainScreenState()
}