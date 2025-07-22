package com.motorro.composeview.view.ui.timer

import androidx.lifecycle.ViewModel
import com.motorro.composeview.appcore.timer.Timer
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.StateFlow
import kotlin.time.Duration

@HiltViewModel
class TimerViewModel @Inject constructor(private val timerFactory: Timer.Factory) : ViewModel() {
    private val timer1: Timer = timerFactory(
        name = "Timer 1"
    )
    private val timer2: Timer = timerFactory(
        name = "Timer 2"
    )

    val title1: String get() = timer1.title
    val title2: String get() = timer2.title
    val count1: StateFlow<Duration> get() = timer1.count
    val count2: StateFlow<Duration> get() = timer2.count
    val isRunning1: StateFlow<Boolean> get() = timer1.isRunning
    val isRunning2: StateFlow<Boolean> get() = timer2.isRunning

    fun start1() = timer1.start()
    fun start2() = timer2.start()
    fun stop1() = timer1.stop()
    fun stop2() = timer2.stop()
    fun reset1() = timer1.reset()
    fun reset2() = timer2.reset()
}