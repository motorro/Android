package com.motorro.composeview.compose.ui.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.motorro.composeview.appcore.timer.Timer
import com.motorro.composeview.appcore.timer.model.TimerViewState
import com.motorro.composeview.appcore.timer.model.viewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(timerFactory: Timer.Factory) : ViewModel() {
    private val timer1: Timer = timerFactory(
        name = "Timer 1"
    )
    private val timer2: Timer = timerFactory(
        name = "Timer 2"
    )

    val viewState1: StateFlow<TimerViewState> = timer1.viewState(1).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        with(timer1) { TimerViewState(1, title, count.value, isRunning.value) }
    )
    val viewState2: StateFlow<TimerViewState> = timer2.viewState(2).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        with(timer2) { TimerViewState(2, title, count.value, isRunning.value) }
    )

    fun start1() = timer1.start()
    fun start2() = timer2.start()
    fun stop1() = timer1.stop()
    fun stop2() = timer2.stop()
    fun reset1() = timer1.reset()
    fun reset2() = timer2.reset()
}