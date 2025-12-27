package com.motorro.tasks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.motorro.composecore.ui.FatalErrorScreen
import com.motorro.composecore.ui.LoadingScreen
import com.motorro.composecore.ui.theme.AppTheme
import com.motorro.core.error.WithRetry
import com.motorro.tasks.app.data.AppGesture
import com.motorro.tasks.app.data.AppUiState
import com.motorro.tasks.login.ui.LoginFlowScreen
import com.motorro.tasks.ui.TaskEditScreen
import com.motorro.tasks.ui.TaskListScreen

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