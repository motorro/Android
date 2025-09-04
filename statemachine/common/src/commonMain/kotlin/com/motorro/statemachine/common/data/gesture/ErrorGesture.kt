package com.motorro.statemachine.common.data.gesture

/**
 * Gestures for the error screen
 */
sealed class ErrorGesture {
    /**
     * Default action
     */
    data object Action : ErrorGesture()

    /**
     * Back-pressed
     */
    data object Back : ErrorGesture()
}