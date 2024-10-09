package ru.merionet.tasks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import ru.merionet.tasks.login.data.LoginGesture
import ru.merionet.tasks.login.data.LoginUiState
import ru.merionet.tasks.login.view.LoginScreen
import ru.merionet.tasks.ui.LoadingScreen
import ru.merionet.tasks.ui.theme.AppTheme

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