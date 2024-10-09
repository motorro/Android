package com.motorro.tasks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.motorro.composecore.ui.LoadingScreen
import com.motorro.composecore.ui.theme.AppTheme
import com.motorro.tasks.login.data.LoginGesture
import com.motorro.tasks.login.data.LoginUiState
import com.motorro.tasks.login.view.LoginScreen

@Composable
fun MainScreen(state: LoginUiState, onComplete: () -> Unit, onGesture: (LoginGesture) -> Unit) {
    AppTheme {
        when(state) {
            is LoginUiState.Loading -> LoadingScreen {
                onGesture(LoginGesture.Back)
            }
            is LoginUiState.Form -> LoginScreen(state, onGesture)
            LoginUiState.Terminated -> LaunchedEffect(Unit) {
                onComplete()
            }
        }
    }
}