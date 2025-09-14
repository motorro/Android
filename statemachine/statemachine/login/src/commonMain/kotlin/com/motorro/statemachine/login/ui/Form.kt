package com.motorro.statemachine.login.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.motorro.statemachine.common.ui.LoginFormScreen
import com.motorro.statemachine.common.ui.ScreenScaffold
import com.motorro.statemachine.login.Res
import com.motorro.statemachine.login.data.LoginFlowGesture
import com.motorro.statemachine.login.data.LoginFlowUiState
import com.motorro.statemachine.login.title_login
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun Form(state: LoginFlowUiState.Form, onGesture: (LoginFlowGesture) -> Unit) {
    ScreenScaffold(
        title = { Text(stringResource(Res.string.title_login)) },
        onBack = { onGesture(LoginFlowGesture.Back) },
        content = { paddingValues ->
            LoginFormScreen(
                state = com.motorro.statemachine.common.data.ui.LoginUiState.Form(state.username, state.password, state.loginEnabled),
                modifier = Modifier.padding(paddingValues),
                onGesture = {
                    when(it) {
                        com.motorro.statemachine.common.data.gesture.LoginGesture.Action -> onGesture(LoginFlowGesture.Action)
                        com.motorro.statemachine.common.data.gesture.LoginGesture.Back -> onGesture(LoginFlowGesture.Back)
                        is com.motorro.statemachine.common.data.gesture.LoginGesture.PasswordChanged -> onGesture(LoginFlowGesture.PasswordChanged(it.value))
                        is com.motorro.statemachine.common.data.gesture.LoginGesture.UsernameChanged -> onGesture(LoginFlowGesture.UsernameChanged(it.value))
                    }
                }
            )
        }
    )
}
