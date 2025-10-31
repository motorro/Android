package com.motorro.background.client.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.motorro.background.client.R
import com.motorro.background.timer.data.TimerGesture
import com.motorro.background.timer.data.TimerState

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MainScreen(state: TimerState, onGesture: (TimerGesture) -> Unit, onTerminated: () -> Unit) {
    BackHandler { onTerminated() }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.app_name)) })
        },
        modifier = Modifier.fillMaxSize(),
        content = { contentPadding ->
            TimerScreen(state, onGesture, Modifier.padding(contentPadding))
        }
    )
}
