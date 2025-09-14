package com.motorro.statemachine.login.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.motorro.statemachine.common.data.gesture.LoadingGesture
import com.motorro.statemachine.common.data.ui.LoadingUiState
import com.motorro.statemachine.common.ui.LoadingScreen
import com.motorro.statemachine.common.ui.ScreenScaffold
import com.motorro.statemachine.login.Res
import com.motorro.statemachine.login.data.LoginFlowGesture
import com.motorro.statemachine.login.logging_in
import com.motorro.statemachine.login.title_logging_in
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun LoggingIn(message: String?, onGesture: (LoginFlowGesture) -> Unit) {
    ScreenScaffold(
        title = { Text(stringResource(Res.string.title_logging_in)) },
        content = { paddingValues ->
            LoadingScreen(LoadingUiState(message ?: stringResource(Res.string.logging_in)), Modifier.padding(paddingValues)) {
                when (it) {
                    LoadingGesture.Back -> onGesture(LoginFlowGesture.Back)
                }
            }
        }
    )
}
