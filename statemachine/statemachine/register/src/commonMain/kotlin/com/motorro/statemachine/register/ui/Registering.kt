package com.motorro.statemachine.register.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.motorro.statemachine.common.data.gesture.LoadingGesture
import com.motorro.statemachine.common.data.ui.LoadingUiState
import com.motorro.statemachine.common.ui.LoadingScreen
import com.motorro.statemachine.common.ui.ScreenScaffold
import com.motorro.statemachine.register.Res
import com.motorro.statemachine.register.data.RegisterFlowGesture
import com.motorro.statemachine.register.registering
import com.motorro.statemachine.register.title_registering
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun Registering(message: String?, onGesture: (RegisterFlowGesture) -> Unit) {
    ScreenScaffold(
        title = { Text(stringResource(Res.string.title_registering)) },
        content = { paddingValues ->
            LoadingScreen(LoadingUiState(message ?: stringResource(Res.string.registering)), Modifier.padding(paddingValues)) {
                when (it) {
                    LoadingGesture.Back -> onGesture(RegisterFlowGesture.Back)
                }
            }
        }
    )
}
