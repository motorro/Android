package com.motorro.composeview.appcore.timer.model

import com.motorro.composeview.appcore.timer.Timer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlin.time.Duration

/**
 * Timer view state
 */
data class TimerViewState(
    val id: Int,
    val title: String,
    val count: Duration,
    val isRunning: Boolean
)

/**
 * Combine timer data to the view-state
 */
fun Timer.viewState(id: Int): Flow<TimerViewState> = combine(count, isRunning) { c, s ->
    TimerViewState(
        id = id,
        title = title,
        count = c,
        isRunning = s
    )
}