package com.motorro.statemachine.commonpreview.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.motorro.statemachine.common.data.ui.ContentScreenUiState
import com.motorro.statemachine.common.ui.ContentScreen

@Composable
fun ContentPreview(modifier: Modifier = Modifier) {
    ContentScreen(ContentScreenUiState("Ivan Petrov"), modifier) {

    }
}