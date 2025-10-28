package com.motorro.background.timer.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.motorro.background.timer.R
import com.motorro.background.timer.data.TimerGesture
import com.motorro.background.timer.data.TimerState
import com.motorro.composecore.ui.theme.AppDimens
import com.motorro.composecore.ui.theme.AppTheme
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Composable
fun TimerView(modifier: Modifier = Modifier, title: String?, state: TimerState, onGesture: (TimerGesture) -> Unit) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background
    ) {
        Card(modifier = Modifier.padding(AppDimens.margin_all)) {
            Column(
                modifier = Modifier
                    .padding(AppDimens.margin_all)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(style = MaterialTheme.typography.headlineLarge, text = title.orEmpty())
                Text(style = MaterialTheme.typography.displayLarge, text = state.time.format())
                Button(onClick = { onGesture(TimerGesture.Toggle) }) {
                    Text(
                        style = MaterialTheme.typography.labelMedium,
                        text = when (state) {
                            is TimerState.Running -> stringResource(R.string.btn_stop)
                            is TimerState.Stopped -> stringResource(R.string.btn_start)
                        }
                    )
                }
            }
        }
    }
}

private fun Duration.format(): String {
    fun Number.pad(): String = toString().padStart(2, '0')
    return toComponents { h, m, s, _ ->
        "${h.pad()}:${m.pad()}:${s.pad()}"
    }
}

@Composable
@Preview(widthDp = 440)
private fun StoppedPreview() {
    AppTheme {
        TimerView(title = "Timer", state = TimerState.Stopped(10.seconds), onGesture = {})
    }
}

@Composable
@Preview(widthDp = 440)
private fun RunningPreview() {
    AppTheme {
        TimerView(title = "Timer", state = TimerState.Running(10.seconds), onGesture = {})
    }
}
