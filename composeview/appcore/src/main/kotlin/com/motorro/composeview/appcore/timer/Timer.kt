package com.motorro.composeview.appcore.timer

import dagger.Module
import dagger.Provides
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.InstallIn
import dagger.hilt.android.ViewModelLifecycle
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Qualifier
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

    @AssistedFactory
    interface Factory {
        operator fun invoke(name: String): TimerImplementation
    }
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ForTimer

/**
 * Timer implementation
 * @property title Timer title
 * @param scope Coroutine scope to run
 * @param delayMillis Timer delay in milliseconds
 */
class TimerImplementation @AssistedInject constructor(
    @param:Assisted override val title: String,
    @param:ForTimer private val scope: CoroutineScope,
    @param:ForTimer private val delayMillis: Long
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

@Module
@InstallIn(ViewModelComponent::class)
class TimerModule {
    /**
     * Timer delay
     */
    @ForTimer
    @Provides
    fun delay() = 100L

    @ForTimer
    @Provides
    fun viewModelScope(lifecycle: ViewModelLifecycle): CoroutineScope {
        val scope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())
        lifecycle.addOnClearedListener { scope.cancel() }
        return scope
    }
}