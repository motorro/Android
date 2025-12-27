package com.motorro.tasks.app.data

import com.motorro.tasks.data.TaskId
import com.motorro.tasks.login.data.LoginGesture
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

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
         * Task add clicked
         */
        data object AddClicked: TaskList()

        /**
         * Task clicked
         */
        data class TaskClicked(val id: TaskId): TaskList()

        /**
         * Task toggled
         */
        data class TaskToggled(val id: TaskId): TaskList()
    }

    /**
     * Task screen
     */
    sealed class EditTask : AppGesture() {
        /**
         * Title changed
         */
        data class TitleChanged(val value: String) : EditTask()

        /**
         * Description changed
         */
        data class DescriptionChanged(val value: String) : EditTask()

        /**
         * Date selected
         */
        data class DateSelected(val value: LocalDate) : EditTask()

        /**
         * Time selected
         */
        data class TimeSelected(val value: LocalTime) : EditTask()

        /**
         * Complete button clicked
         */
        data object CompleteClicked : EditTask()

        /**
         * Save button clicked
         */
        data object SaveClicked : EditTask()
    }
}