package com.motorro.statemachine.common.data.gesture

/**
 * Authentication prompt gestures
 */
sealed class AuthPromptGesture {
    /**
     * Back gesture
     */
    data object Back : AuthPromptGesture()

    /**
     * Login clicked
     */
    data object LoginClicked : AuthPromptGesture()

    /**
     * Registration clicked
     */
    data object RegistrationClicked : AuthPromptGesture()
}