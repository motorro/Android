package com.motorro.tasks.app.data

import com.motorro.tasks.data.TaskId
import com.motorro.tasks.login.data.LoginGesture

/**
 * Application gesture
 */
sealed class AppGesture {
    /**
     * Back pressed
     */
    data object Back : AppGesture()

    /**
     * Default action
     */
    data object Action : AppGesture()

    /**
     * Refresh
     */
    data object Refresh : AppGesture()

    /**
     * Dismiss current error
     */
    data object DismissError : AppGesture()

    /**
     * Login flow gesture
     */
    data class Login(val child: LoginGesture) : AppGesture()

    /**
     * Logout
     */
    data object Logout : AppGesture()

    /**
     * Task-list gesture
     */
    sealed class TaskList : AppGesture() {
        /**
         * Task clicked
         */
        data class TaskClicked(val id: TaskId): TaskList()

        /**
         * Task toggled
         */
        data class TaskToggled(val id: TaskId): TaskList()
    }
}