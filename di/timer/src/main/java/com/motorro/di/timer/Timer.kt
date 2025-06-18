package com.motorro.di.timer

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

/**
 * Timer interface
 */
interface Timer {
    /**
     * Timer title
     */
    val title: String

    /**
     * Timer count
     */
    val count: StateFlow<Duration>

    /**
     * Timer is running
     */
    val isRunning: StateFlow<Boolean>

    /**
     * Starts timer
     */
    fun start()

    /**
     * Stops timer
     */
    fun stop()

    /**
     * Resets timer
     */
    fun reset()
}

/**
 * Timer implementation
 * @property title Timer title
 * @param scope Coroutine scope to run
 * @param delayMillis Timer delay in milliseconds
 */
class TimerImplementation(
    override val title: String,
    private val scope: CoroutineScope,
    private val delayMillis: Long
) : Timer {

    private val _count = MutableStateFlow(0.seconds)
    private val _isRunning = MutableStateFlow(false)
    private var _job: Job? = null

    override val count: StateFlow<Duration> get() = _count.asStateFlow()
    override val isRunning: StateFlow<Boolean> get() = _isRunning.asStateFlow()

    override fun start() = synchronized(this) {
        if (_isRunning.value) return@synchronized
        _job = scope.launch {
            while (isActive) {
                delay(delayMillis.milliseconds)
                _count.update { it + delayMillis.milliseconds }
            }
        }
        _isRunning.value = true
    }

    override fun stop() = synchronized(this) {
        if (_isRunning.value.not()) return@synchronized
        _job?.cancel()
        _job = null
        _isRunning.value = false
    }

    override fun reset() {
        _count.value = 0.seconds
    }
}