package com.motorro.composeview.compose.ui.timer.data

import androidx.compose.runtime.Immutable
import com.motorro.composeview.appcore.timer.model.TimerViewState

@Immutable
data class TimerScreenViewState(
    val timer1: TimerViewState,
    val timer2: TimerViewState
)