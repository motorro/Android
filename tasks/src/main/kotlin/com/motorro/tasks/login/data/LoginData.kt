package com.motorro.tasks.login.data

/**
 * Inter-state data for login flow
 * Passed between machine-states
 */
data class LoginData(
    val userName: String = "",
    val password: String = "",
    val message: String? = null
)