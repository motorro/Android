package com.motorro.statemachine.statemachine.ui

import android.statemachine.statemachine.composeapp.generated.resources.Res
import android.statemachine.statemachine.composeapp.generated.resources.title_loading
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.motorro.statemachine.common.data.ui.LoadingUiState
import com.motorro.statemachine.common.ui.LoadingScreen
import com.motorro.statemachine.common.ui.ScreenScaffold
import com.motorro.statemachine.statemachine.data.AppGesture
import org.jetbrains.compose.resources.stringResource

@Composable
fun LoadingScreen(message: String, onGesture: (AppGesture) -> Unit) {
    ScreenScaffold(
        title = { Text(stringResource(Res.string.title_loading)) },
        content = { paddingValues ->
            LoadingScreen(LoadingUiState(message), Modifier.padding(paddingValues)) {
                when(it) {
                    com.motorro.statemachine.common.data.gesture.LoadingGesture.Back -> onGesture(AppGesture.Back)
                }
            }
        }
    )
}