package ru.merionet.tasks.app.data

import ru.merionet.tasks.data.Task
import ru.merionet.tasks.login.data.LoginUiState

/**
 * Application UI state
 */
sealed class AppUiState {
    /**
     * Full-screen loading
     * @param message Loading description
     */
    data class Loading(val message: String? = null) : AppUiState()

    /**
     * Delegating view-state when view is handled with Login flow
     */
    data class LoggingIn(val child: LoginUiState) : AppUiState()

    /**
     * Task list screen
     */
    data class TaskList(
        val tasks: Collection<Task>,
        val isLoading: Boolean,
        val error: AppError?
    ) : AppUiState()

    /**
     * Fatal application error
     * @property error Occurred error
     */
    data class Error(val error: Throwable) : AppUiState()

    /**
     * Application terminated
     */
    data object Terminated : AppUiState()
}