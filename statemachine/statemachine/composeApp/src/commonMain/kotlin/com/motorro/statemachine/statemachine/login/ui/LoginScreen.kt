package com.motorro.statemachine.statemachine.login.ui

import android.statemachine.statemachine.composeapp.generated.resources.Res
import android.statemachine.statemachine.composeapp.generated.resources.title_error
import android.statemachine.statemachine.composeapp.generated.resources.title_login
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.motorro.statemachine.common.ui.ErrorScreen
import com.motorro.statemachine.common.ui.LoginFormScreen
import com.motorro.statemachine.common.ui.ScreenScaffold
import com.motorro.statemachine.statemachine.data.AppGesture
import com.motorro.statemachine.statemachine.data.LoginGesture
import com.motorro.statemachine.statemachine.data.LoginUiState
import org.jetbrains.compose.resources.stringResource

@Composable
fun LoginScreen(state: LoginUiState, onGesture: (AppGesture) -> Unit) {
    when(state) {
        is LoginUiState.Form -> Form(state, onGesture)
        is LoginUiState.Error -> Error(state, onGesture)
    }
}

@Composable
private fun Form(state: LoginUiState.Form, onGesture: (AppGesture) -> Unit) {
    ScreenScaffold(
        title = { Text(stringResource(Res.string.title_login)) },
        onBack = { onGesture(AppGesture.Back) },
        content = { paddingValues ->
            LoginFormScreen(
                state = com.motorro.statemachine.common.data.ui.LoginUiState.Form(state.username, state.password, state.loginEnabled),
                modifier = Modifier.padding(paddingValues),
                onGesture = {
                    when(it) {
                        com.motorro.statemachine.common.data.gesture.LoginGesture.Action -> onGesture(AppGesture.Action)
                        com.motorro.statemachine.common.data.gesture.LoginGesture.Back -> onGesture(AppGesture.Back)
                        is com.motorro.statemachine.common.data.gesture.LoginGesture.PasswordChanged -> onGesture(LoginGesture.PasswordChanged(it.value))
                        is com.motorro.statemachine.common.data.gesture.LoginGesture.UsernameChanged -> onGesture(LoginGesture.UsernameChanged(it.value))
                    }
                }
            )
        }
    )
}

@Composable
private fun Error(state: LoginUiState.Error, onGesture: (AppGesture) -> Unit) {
    ScreenScaffold(
        title = { Text(stringResource(Res.string.title_error)) },
        onBack = { onGesture(AppGesture.Back) },
        content = { paddingValues ->
            ErrorScreen(
                error = com.motorro.statemachine.common.data.ui.ErrorUiState(state.message, state.canRetry),
                modifier = Modifier.padding(paddingValues),
                onGesture = {
                    when(it) {
                        com.motorro.statemachine.common.data.gesture.ErrorGesture.Action -> onGesture(AppGesture.Action)
                        com.motorro.statemachine.common.data.gesture.ErrorGesture.Back -> onGesture(AppGesture.Back)
                    }
                }
            )
        }
    )
}