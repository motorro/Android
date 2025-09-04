package com.motorro.statemachine.common.data.ui

/**
 * Error screen data
 * @property message The error message
 * @property canRetry Whether the error can be retried
 */
data class ErrorUiState(val message: String, val canRetry: Boolean = false)