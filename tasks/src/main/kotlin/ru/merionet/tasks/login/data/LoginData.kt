package ru.merionet.tasks.login.data

import ru.merionet.tasks.login.LoginFlowHost

/**
 * Inter-state data for login flow
 * Passed between machine-states
 * @property flowHost Hosting flow to call when flow completes
 * @property userName Username
 * @property password Password
 * @property message Login prompt message if any
 */
data class LoginData(
    val flowHost: LoginFlowHost,
    val userName: String = "",
    val password: String = "",
    val message: String? = null
)