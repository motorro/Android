package ru.merionet.tasks.login.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.jetsmarter.composecore.ui.FatalErrorScreen
import com.jetsmarter.composecore.ui.LoadingScreen
import com.jetsmarter.composecore.ui.theme.AppTheme
import ru.merionet.tasks.R
import ru.merionet.tasks.auth.data.SessionError
import ru.merionet.tasks.login.data.LoginGesture
import ru.merionet.tasks.login.data.LoginUiState

/**
 * Login flow UI
 */
@Composable
fun LoginFlowScreen(state: LoginUiState, onGesture: (LoginGesture) -> Unit) {
    AppTheme {
        when(state) {
            is LoginUiState.Loading -> LoadingScreen(message = stringResource(R.string.msg_logging_in)) {
                onGesture(LoginGesture.Back)
            }
            is LoginUiState.Form -> LoginScreen(state, onGesture)
            is LoginUiState.LoginError -> FatalErrorScreen(
                error = state.error.getText(),
                retriable = state.error.retriable,
                onDismiss = { onGesture(LoginGesture.Action) },
                onBack = { onGesture(LoginGesture.Back) }
            )
        }
    }
}

@Composable
private fun SessionError.getText(): String = when(this) {
    is SessionError.Authentication -> stringResource(R.string.err_authentication, message)
    is SessionError.Network -> stringResource(R.string.err_network)
    is SessionError.Storage -> stringResource(R.string.err_storage)
}