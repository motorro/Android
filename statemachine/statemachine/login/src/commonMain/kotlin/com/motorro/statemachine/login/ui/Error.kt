package com.motorro.statemachine.login.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.motorro.statemachine.common.ui.ErrorScreen
import com.motorro.statemachine.common.ui.ScreenScaffold
import com.motorro.statemachine.login.Res
import com.motorro.statemachine.login.data.LoginFlowGesture
import com.motorro.statemachine.login.data.LoginFlowUiState
import com.motorro.statemachine.login.title_error
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun Error(state: LoginFlowUiState.Error, onGesture: (LoginFlowGesture) -> Unit) {
    ScreenScaffold(
        title = { Text(stringResource(Res.string.title_error)) },
        onBack = { onGesture(LoginFlowGesture.Back) },
        content = { paddingValues ->
            ErrorScreen(
                error = com.motorro.statemachine.common.data.ui.ErrorUiState(state.message, state.canRetry),
                modifier = Modifier.padding(paddingValues),
                onGesture = {
                    when(it) {
                        com.motorro.statemachine.common.data.gesture.ErrorGesture.Action -> onGesture(LoginFlowGesture.Action)
                        com.motorro.statemachine.common.data.gesture.ErrorGesture.Back -> onGesture(LoginFlowGesture.Back)
                    }
                }
            )
        }
    )
}