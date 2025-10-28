package com.motorro.background.pages.service.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.motorro.background.pages.service.data.ServiceGesture
import com.motorro.background.pages.service.data.ServiceUiState
import com.motorro.background.timer.data.TimerState
import com.motorro.background.timer.ui.TimerView
import com.motorro.composecore.ui.theme.AppDimens
import com.motorro.composecore.ui.theme.AppTheme
import kotlin.time.Duration.Companion.seconds

/**
 * Service screen
 * @param modifier Modifier
 * @param state UI state
 * @param onGesture Gesture callback
 */
@Composable
fun ServiceScreen(
    state: ServiceUiState,
    onGesture: (ServiceGesture) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().padding(AppDimens.margin_all)
    ) {
        TimerView(
            modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally),
            title = "Service timer",
            state = state.timerState,
            onGesture = { onGesture(ServiceGesture.Timer(it)) }
        )
    }
}

@Composable
@Preview(widthDp = 440, showBackground = true)
private fun ServiceScreenPreview() {
    AppTheme {
        ServiceScreen(
            state = ServiceUiState(TimerState.Stopped(10.seconds)),
            onGesture = {}
        )
    }
}
