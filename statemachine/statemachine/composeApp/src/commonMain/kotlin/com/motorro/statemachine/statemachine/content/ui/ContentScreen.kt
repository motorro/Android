package com.motorro.statemachine.statemachine.content.ui

import android.statemachine.statemachine.composeapp.generated.resources.Res
import android.statemachine.statemachine.composeapp.generated.resources.title_content
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.motorro.statemachine.common.data.ui.ContentScreenUiState
import com.motorro.statemachine.common.ui.ContentScreen
import com.motorro.statemachine.common.ui.ScreenScaffold
import com.motorro.statemachine.statemachine.data.AppGesture
import com.motorro.statemachine.statemachine.data.ContentGesture
import com.motorro.statemachine.statemachine.data.ContentUiState
import org.jetbrains.compose.resources.stringResource

@Composable
fun ContentScreen(state: ContentUiState, onGesture: (AppGesture) -> Unit) {
    ScreenScaffold(
        title = { Text(stringResource(Res.string.title_content)) },
        onBack = { onGesture(AppGesture.Back) },
        content = { paddingValues ->
            ContentScreen(ContentScreenUiState(state.username), Modifier.padding(paddingValues)) {
                when(it) {
                    com.motorro.statemachine.common.data.gesture.ContentGesture.Back -> onGesture(AppGesture.Back)
                    com.motorro.statemachine.common.data.gesture.ContentGesture.Logout -> onGesture(ContentGesture.Logout)
                }
            }
        }
    )
}