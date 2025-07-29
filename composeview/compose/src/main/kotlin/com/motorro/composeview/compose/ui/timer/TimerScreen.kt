package com.motorro.composeview.compose.ui.timer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.motorro.composeview.appcore.timer.model.TimerGesture
import com.motorro.composeview.compose.ui.TimerView
import com.motorro.composeview.appcore.R as RC

@Composable
fun TimerScreen(viewModel: TimerViewModel, modifier: Modifier = Modifier) {

    val timerState1 by viewModel.viewState1.collectAsStateWithLifecycle()
    val timerState2 by viewModel.viewState2.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(RC.dimen.activity_horizontal_margin)),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        TimerView(state = timerState1, Modifier.fillMaxWidth()) { _, gesture ->
            when(gesture) {
                TimerGesture.StartPressed -> viewModel.start1()
                TimerGesture.StopPressed -> viewModel.stop1()
                TimerGesture.ResetPressed -> viewModel.reset1()
            }
        }
        TimerView(state = timerState2, Modifier.fillMaxWidth()) { _, gesture ->
            when(gesture) {
                TimerGesture.StartPressed -> viewModel.start2()
                TimerGesture.StopPressed -> viewModel.stop2()
                TimerGesture.ResetPressed -> viewModel.reset2()
            }
        }
    }
}