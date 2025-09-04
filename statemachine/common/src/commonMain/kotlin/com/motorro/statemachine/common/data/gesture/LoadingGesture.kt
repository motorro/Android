package com.motorro.statemachine.common.data.gesture

/**
 * Gestures for loading screen
 */
sealed class LoadingGesture {
    /**
     * Back pressed
     */
    data object Back : LoadingGesture()
}