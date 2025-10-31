package com.motorro.background.client.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.motorro.background.client.R
import com.motorro.background.timer.data.TimerGesture
import com.motorro.background.timer.data.TimerState
import com.motorro.background.timer.ui.TimerView
import com.motorro.composecore.ui.theme.AppDimens
import com.motorro.composecore.ui.theme.AppTheme
import kotlin.time.Duration.Companion.seconds

/**
 * Timer screen
 * @param modifier Modifier
 * @param state UI state
 * @param onGesture Gesture callback
 */
@Composable
fun TimerScreen(
    state: TimerState,
    onGesture: (TimerGesture) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(AppDimens.margin_all),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(AppDimens.spacer)
    ) {
        TimerView(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(R.string.title_service_timer),
            state = state,
            onGesture = onGesture
        )
    }
}

@Composable
@Preview(widthDp = 440, showBackground = true)
private fun ServiceScreenPreviewStopped() {
    AppTheme {
        TimerScreen(
            state = TimerState.Stopped(10.seconds),
            onGesture = {}
        )
    }
}

@Composable
@Preview(widthDp = 440, showBackground = true)
private fun ServiceScreenPreviewRunning() {
    AppTheme {
        TimerScreen(
            state = TimerState.Running(10.seconds),
            onGesture = {}
        )
    }
}
