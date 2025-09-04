package com.motorro.statemachine.common.data.gesture

/**
 * Content screen gesture
 */
sealed class ContentGesture {
    /**
     * Back gesture
     */
    object Back : ContentGesture()

    /**
     * Logout gesture
     */
    object Logout : ContentGesture()
}