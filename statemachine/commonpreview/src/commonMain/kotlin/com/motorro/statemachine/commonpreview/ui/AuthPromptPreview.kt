package com.motorro.statemachine.commonpreview.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.motorro.statemachine.common.data.ui.AuthPromptUiState
import com.motorro.statemachine.common.ui.AuthPromptScreen

@Composable
fun AuthPromptPreview(modifier: Modifier = Modifier) {
    AuthPromptScreen(AuthPromptUiState, modifier) {

    }
}