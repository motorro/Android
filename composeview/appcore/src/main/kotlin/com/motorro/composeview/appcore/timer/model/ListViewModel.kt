package com.motorro.composeview.appcore.timer.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.motorro.composeview.appcore.timer.Timer
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class ListViewModel @Inject constructor(factory: Timer.Factory) : ViewModel() {

    private val timers: List<Timer> = (1 .. NUM_OF_TIMERS).map {
        factory("Timer $it")
    }

    val viewStates: StateFlow<List<TimerViewState>> = combine(
        flows = timers.mapIndexed { i, t -> t.viewState(i + 1) },
        transform = { it.toList() }
    ).stateIn(viewModelScope, SharingStarted.Companion.WhileSubscribed(), emptyList())

    fun process(timerId: Int, gesture: TimerGesture) = with(timers[timerId - 1]) {
        when(gesture) {
            TimerGesture.StartPressed -> start()
            TimerGesture.StopPressed -> stop()
            TimerGesture.ResetPressed -> reset()
        }
    }

    companion object {
        const val NUM_OF_TIMERS = 10
    }
}