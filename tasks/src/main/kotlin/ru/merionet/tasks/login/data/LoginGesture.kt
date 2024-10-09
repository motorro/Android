package ru.merionet.tasks.login.data

/**
 * Login gestures - a collection of all gestures the user may invoke while going through login flow.
 * Update direction: UI -> Model
 */
sealed class LoginGesture {
    /**
     * Back pressed
     */
    data object Back : LoginGesture()

    /**
     * Default action button pressed
     */
    data object Action : LoginGesture()

    /**
     * User-name field changed
     * @property value New field value
     */
    data class UserNameChanged(val value: String) : LoginGesture()

    /**
     * Password field changed
     * @property value New field value
     */
    data class PasswordChanged(val value: String) : LoginGesture()
}