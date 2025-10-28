package com.motorro.background.pages.service.data

import com.motorro.background.timer.data.TimerState

data class ServiceUiState(val timerState: TimerState) {
    companion object {
        val EMPTY = ServiceUiState(TimerState.Stopped())
    }
}