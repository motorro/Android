package com.motorro.composeview.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.motorro.composeview.compose.ui.timer.TimerScreen

@Composable
fun ComposeApp() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { contentPadding ->
            TimerScreen(hiltViewModel(), Modifier.padding(contentPadding))
        }
    )
}