package com.motorro.background.timer.data

import android.os.Parcelable
import com.motorro.background.timer.ITimerState
import kotlinx.parcelize.Parcelize
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

/**
 * Timer state
 */
@Parcelize
sealed class TimerState : Parcelable, ITimerState.Stub() {
    /**
     * Timer count
     */
    abstract val time: Duration

    /**
     * AIDL override
     */
    override fun getEpochMillis(): Long = time.inWholeMilliseconds

    /**
     * Running
     */
    data class Running(override val time: Duration) : TimerState() {
        override fun isRunning(): Boolean = true
    }

    /**
     * Stopped
     */
    data class Stopped(override val time: Duration = 0.seconds) : TimerState() {
        override fun isRunning(): Boolean = false
    }
}

/**
 * Convert [ITimerState] to [TimerState]
 */
fun ITimerState.toTimerState(): TimerState = when(isRunning) {
    true -> TimerState.Running(epochMillis.milliseconds)
    false -> TimerState.Stopped(epochMillis.milliseconds)
}
