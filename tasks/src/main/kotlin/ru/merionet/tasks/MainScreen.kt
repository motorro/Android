package ru.merionet.tasks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.jetsmarter.composecore.ui.FatalErrorScreen
import com.jetsmarter.composecore.ui.LoadingScreen
import com.jetsmarter.composecore.ui.theme.AppTheme
import ru.merionet.core.error.WithRetry
import ru.merionet.tasks.app.data.AppGesture
import ru.merionet.tasks.app.data.AppUiState
import ru.merionet.tasks.login.ui.LoginFlowScreen
import ru.merionet.tasks.ui.TaskEditScreen
import ru.merionet.tasks.ui.TaskListScreen

@Composable
fun MainScreen(state: AppUiState, onComplete: () -> Unit, onGesture: (AppGesture) -> Unit) {
    AppTheme {
        when(state) {
            is AppUiState.Loading -> LoadingScreen {
                onGesture(AppGesture.Back)
            }
            is AppUiState.LoggingIn -> LoginFlowScreen(state.child) {
                onGesture(AppGesture.Login(it))
            }
            is AppUiState.TaskList -> TaskListScreen(state, onGesture)
            is AppUiState.EditTask -> TaskEditScreen(state, onGesture)
            is AppUiState.Error -> FatalErrorScreen(
                error = state.error.message,
                retriable = true == (state.error as? WithRetry)?.retriable,
                onDismiss = { onGesture(AppGesture.Action) },
                onBack = { onGesture(AppGesture.Back) }
            )
            AppUiState.Terminated -> LaunchedEffect(Unit) {
                onComplete()
            }
        }
    }
}