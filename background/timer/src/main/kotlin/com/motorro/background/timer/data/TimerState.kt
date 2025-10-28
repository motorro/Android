package com.motorro.background.timer.data

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * Timer state
 */
sealed class TimerState {
    /**
     * Timer count
     */
    abstract val time: Duration

    /**
     * Running
     */
    data class Running(override val time: Duration) : TimerState()

    /**
     * Stopped
     */
    data class Stopped(override val time: Duration = 0.seconds) : TimerState()
}