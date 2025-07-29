package com.motorro.composeview.compose.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.motorro.composeview.appcore.timer.model.TimerGesture
import com.motorro.composeview.appcore.timer.model.TimerViewState
import com.motorro.composeview.appcore.timer.toDisplayString
import com.motorro.composeview.compose.ui.theme.AppTheme
import kotlin.time.Duration.Companion.minutes
import com.motorro.composeview.appcore.R as CR


@Composable
fun TimerView(state: TimerViewState, modifier: Modifier = Modifier, onGesture: (Int, TimerGesture) -> Unit) {
    Column(
        modifier = modifier.wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = state.title,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            modifier = Modifier.padding(top = dimensionResource(CR.dimen.activity_vertical_margin)),
            text = state.count.toDisplayString(),
            style = MaterialTheme.typography.displayLarge
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilledIconButton(
                onClick = { onGesture(state.id, TimerGesture.StopPressed) },
                enabled = state.isRunning
            ) {
                Icon(
                    painter = painterResource(CR.drawable.ic_stop),
                    contentDescription = stringResource(CR.string.btn_stop)
                )
            }
            FilledIconButton(
                onClick = { onGesture(state.id, TimerGesture.StartPressed) },
                enabled = state.isRunning.not()
            ) {
                Icon(
                    painter = painterResource(CR.drawable.ic_start),
                    contentDescription = stringResource(CR.string.btn_start)
                )
            }
            FilledIconButton(onClick = { onGesture(state.id, TimerGesture.ResetPressed) }) {
                Icon(
                    painter = painterResource(CR.drawable.ic_reset),
                    contentDescription = stringResource(CR.string.btn_reset)
                )
            }
        }
    }
}

@Preview
@Composable
fun TimerViewPreview() {
    AppTheme {
        TimerView(
            state = TimerViewState(1, "Timer", 1.minutes, true)
        ) { id, gesture -> }
    }
}