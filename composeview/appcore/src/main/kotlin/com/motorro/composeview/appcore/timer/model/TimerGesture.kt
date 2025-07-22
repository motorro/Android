package com.motorro.composeview.appcore.timer.model

/**
 * Timer gestures
 */
sealed class TimerGesture {
    /**
     * Start pressed
     */
    data object StartPressed : TimerGesture()

    /**
     * Stop pressed
     */
    data object StopPressed : TimerGesture()

    /**
     * Reset pressed
     */
    data object ResetPressed : TimerGesture()
}