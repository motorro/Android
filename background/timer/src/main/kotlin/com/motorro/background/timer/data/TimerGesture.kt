package com.motorro.background.timer.data

/**
 * Timer gesture
 */
sealed class TimerGesture {
    /**
     * Toggles timer on/off
     */
    data object Toggle : TimerGesture()
}
