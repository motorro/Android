package com.motorro.background.pages.service.data

import com.motorro.background.timer.data.TimerState

data class ServiceUiState(
    val timerState: TimerState,
    val hasServiceStatus: Boolean,
    val isServiceRunning: Boolean,
    val isServiceBound: Boolean
) {
    companion object {
        val EMPTY = ServiceUiState(
            TimerState.Stopped(),
            hasServiceStatus = false,
            isServiceRunning = false,
            isServiceBound = false
        )
    }
}