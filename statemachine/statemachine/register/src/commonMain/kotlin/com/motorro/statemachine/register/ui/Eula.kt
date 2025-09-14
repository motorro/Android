package com.motorro.statemachine.register.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.motorro.statemachine.common.data.gesture.RegistrationGesture
import com.motorro.statemachine.common.data.ui.RegistrationUiState
import com.motorro.statemachine.common.ui.RegistrationEulaScreen
import com.motorro.statemachine.common.ui.ScreenScaffold
import com.motorro.statemachine.register.Res
import com.motorro.statemachine.register.data.RegisterFlowGesture
import com.motorro.statemachine.register.data.RegisterFlowGesture.Action
import com.motorro.statemachine.register.data.RegisterFlowGesture.Back
import com.motorro.statemachine.register.data.RegisterFlowGesture.EulaToggled
import com.motorro.statemachine.register.data.RegisterFlowUiState
import com.motorro.statemachine.register.title_accept_eula
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun Eula(state: RegisterFlowUiState.Eula, onGesture: (RegisterFlowGesture) -> Unit) {
    ScreenScaffold(
        title = { Text(stringResource(Res.string.title_accept_eula)) },
        onBack = { onGesture(Back) },
        content = { paddingValues ->
            RegistrationEulaScreen(
                state = RegistrationUiState.Eula(state.eula, state.accepted, state.nextEnabled),
                modifier = Modifier.padding(paddingValues),
                onGesture = {
                    when(it) {
                        RegistrationGesture.Action -> onGesture(Action)
                        RegistrationGesture.Back -> onGesture(Back)
                        RegistrationGesture.EulaToggled -> onGesture(EulaToggled)
                        else -> { /* No-op */ }
                    }
                }
            )
        }
    )
}
