package com.motorro.architecture.registration.registering.data

import com.motorro.architecture.core.error.CoreException

/**
 * Registering view-state
 */
sealed class RegisteringViewState {
    data object Loading : RegisteringViewState()
    data object Complete : RegisteringViewState()
    data class Error(val cause: CoreException) : RegisteringViewState()
}