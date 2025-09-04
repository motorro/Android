package com.motorro.statemachine.commonpreview.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.motorro.statemachine.common.data.ui.ErrorUiState
import com.motorro.statemachine.common.ui.ErrorScreen

@Composable
fun ErrorPreview(modifier: Modifier = Modifier) {
    ErrorScreen(ErrorUiState("Some non-critical error", canRetry = true), modifier) {

    }
}