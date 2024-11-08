package com.motorro.lifecycle

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * Duration formatter HH:mm:ss
 */
fun Duration.format(): String {
    fun Number.pad(): String = toString().padStart(2, '0')
    return toComponents{ h, m,s, _ ->
        "${h.pad()}:${m.pad()}:${s.pad()}"
    }
}

/**
 * Timer
 */
class Timer(
    private val scope: CoroutineScope,
    private val update: (State) -> Unit,
    startTime: Duration? = null
) {

    /**
     * Timer state
     */
    data class State(val time: Duration, val running: Boolean)

    private var job: Job? = null

    var time: Duration = startTime ?: Duration.ZERO
        private set

    fun start() {
        job = scope.launch {
            while(currentCoroutineContext().isActive) {
                update(State(time, true))
                time += 1.seconds
                delay(1.seconds)
            }
        }
    }

    fun stop() {
        update(State(time, false))
        job?.cancel()
        job = null
    }
}