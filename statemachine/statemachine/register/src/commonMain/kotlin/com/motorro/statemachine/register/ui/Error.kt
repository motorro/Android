package com.motorro.statemachine.register.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.motorro.statemachine.common.data.gesture.ErrorGesture
import com.motorro.statemachine.common.data.ui.ErrorUiState
import com.motorro.statemachine.common.ui.ErrorScreen
import com.motorro.statemachine.common.ui.ScreenScaffold
import com.motorro.statemachine.register.Res
import com.motorro.statemachine.register.data.RegisterFlowGesture
import com.motorro.statemachine.register.data.RegisterFlowUiState
import com.motorro.statemachine.register.title_error
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun Error(state: RegisterFlowUiState.Error, onGesture: (RegisterFlowGesture) -> Unit) {
    ScreenScaffold(
        title = { Text(stringResource(Res.string.title_error)) },
        onBack = { onGesture(RegisterFlowGesture.Back) },
        content = { paddingValues ->
            ErrorScreen(
                error = ErrorUiState(state.message, state.canRetry),
                modifier = Modifier.padding(paddingValues),
                onGesture = {
                    when(it) {
                        ErrorGesture.Action -> onGesture(RegisterFlowGesture.Action)
                        ErrorGesture.Back -> onGesture(RegisterFlowGesture.Back)
                    }
                }
            )
        }
    )
}