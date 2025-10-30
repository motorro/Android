package com.motorro.background.pages.service.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.motorro.background.R
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
        modifier = modifier
            .fillMaxSize()
            .padding(AppDimens.margin_all),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(AppDimens.spacer)
    ) {
        TimerView(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(R.string.title_service_timer),
            state = state.timerState,
            onGesture = { onGesture(ServiceGesture.Timer(it)) }
        )
        if (state.hasServiceStatus) {
            Column(verticalArrangement = Arrangement.spacedBy(AppDimens.spacer)) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(AppDimens.margin_all)) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(R.string.title_service_launch),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(AppDimens.spacer_small))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(AppDimens.spacer)
                        ) {
                            Button(
                                modifier = Modifier.weight(1f),
                                onClick = { onGesture(ServiceGesture.StartService) },
                                enabled = state.isServiceRunning.not()
                            ) {
                                Text(text = stringResource(R.string.btn_start))
                            }
                            Button(
                                modifier = Modifier.weight(1f),
                                onClick = { onGesture(ServiceGesture.StopService) },
                                enabled = state.isServiceBound.not() && state.isServiceRunning
                            ) {
                                Text(text = stringResource(R.string.btn_stop))
                            }
                        }
                    }
                }
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(AppDimens.margin_all)) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(R.string.title_service_binding),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(AppDimens.spacer_small))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(AppDimens.spacer)
                        ) {
                            Button(
                                modifier = Modifier.weight(1f),
                                onClick = { onGesture(ServiceGesture.BindService) },
                                enabled = state.isServiceBound.not()
                            ) {
                                Text(text = stringResource(R.string.btn_bind))
                            }
                            Button(
                                modifier = Modifier.weight(1f),
                                onClick = { onGesture(ServiceGesture.UnbindService) },
                                enabled = state.isServiceBound
                            ) {
                                Text(text = stringResource(R.string.btn_unbind))
                            }
                        }
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier.padding(AppDimens.margin_all),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(AppDimens.spacer))
                Text(text = stringResource(R.string.title_getting_service_status))
            }
        }
    }
}

@Composable
@Preview(widthDp = 440, showBackground = true)
private fun ServiceScreenPreviewLoading() {
    AppTheme {
        ServiceScreen(
            state = ServiceUiState(
                TimerState.Stopped(10.seconds),
                hasServiceStatus = false,
                isServiceRunning = false,
                isServiceBound = false
            ),
            onGesture = {}
        )
    }
}

@Composable
@Preview(widthDp = 440, showBackground = true)
private fun ServiceScreenPreviewStopped() {
    AppTheme {
        ServiceScreen(
            state = ServiceUiState(
                TimerState.Stopped(10.seconds),
                hasServiceStatus = true,
                isServiceRunning = false,
                isServiceBound = false
            ),
            onGesture = {}
        )
    }
}

@Composable
@Preview(widthDp = 440, showBackground = true)
private fun ServiceScreenPreviewRunningUnbound() {
    AppTheme {
        ServiceScreen(
            state = ServiceUiState(
                TimerState.Running(10.seconds),
                hasServiceStatus = true,
                isServiceRunning = true,
                isServiceBound = false
            ),
            onGesture = {}
        )
    }
}

@Composable
@Preview(widthDp = 440, showBackground = true)
private fun ServiceScreenPreviewRunningBound() {
    AppTheme {
        ServiceScreen(
            state = ServiceUiState(
                TimerState.Running(10.seconds),
                hasServiceStatus = true,
                isServiceRunning = true,
                isServiceBound = true
            ),
            onGesture = {}
        )
    }
}
