package com.motorro.statemachine.register.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.motorro.statemachine.common.data.gesture.RegistrationGesture
import com.motorro.statemachine.common.data.ui.RegistrationUiState
import com.motorro.statemachine.common.ui.RegistrationFormScreen
import com.motorro.statemachine.common.ui.ScreenScaffold
import com.motorro.statemachine.register.Res
import com.motorro.statemachine.register.data.RegisterFlowGesture
import com.motorro.statemachine.register.data.RegisterFlowGesture.Action
import com.motorro.statemachine.register.data.RegisterFlowGesture.Back
import com.motorro.statemachine.register.data.RegisterFlowGesture.EmailChanged
import com.motorro.statemachine.register.data.RegisterFlowGesture.PasswordChanged
import com.motorro.statemachine.register.data.RegisterFlowGesture.UsernameChanged
import com.motorro.statemachine.register.data.RegisterFlowUiState
import com.motorro.statemachine.register.title_register
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun Form(state: RegisterFlowUiState.Form, onGesture: (RegisterFlowGesture) -> Unit) {
    ScreenScaffold(
        title = { Text(stringResource(Res.string.title_register)) },
        onBack = { onGesture(RegisterFlowGesture.Back) },
        content = { paddingValues ->
            RegistrationFormScreen(
                state = RegistrationUiState.Form(state.username, state.password, state.email, state.nextEnabled),
                modifier = Modifier.padding(paddingValues),
                onGesture = {
                    when(it) {
                        RegistrationGesture.Action -> onGesture(Action)
                        RegistrationGesture.Back -> onGesture(Back)
                        is RegistrationGesture.UsernameChanged -> onGesture(UsernameChanged(it.value))
                        is RegistrationGesture.PasswordChanged -> onGesture(PasswordChanged(it.value))
                        is RegistrationGesture.EmailChanged -> onGesture(EmailChanged(it.value))
                        else -> { /* No-op */ }
                    }
                }
            )
        }
    )
}
