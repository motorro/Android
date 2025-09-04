package com.motorro.statemachine.commonpreview.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.motorro.statemachine.common.data.ui.LoadingUiState
import com.motorro.statemachine.common.ui.LoadingScreen

@Composable
fun LoadingPreview(modifier: Modifier = Modifier) {
    LoadingScreen(LoadingUiState(), modifier) {

    }
}