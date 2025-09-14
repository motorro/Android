package com.motorro.statemachine.statemachine

import android.statemachine.statemachine.composeapp.generated.resources.Res
import android.statemachine.statemachine.composeapp.generated.resources.loading
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.motorro.statemachine.auth.AuthScreen
import com.motorro.statemachine.common.ui.theme.AndroidTheme
import com.motorro.statemachine.statemachine.content.ui.ContentScreen
import com.motorro.statemachine.statemachine.data.AppGesture
import com.motorro.statemachine.statemachine.data.AppUiState
import com.motorro.statemachine.statemachine.data.AuthGesture
import com.motorro.statemachine.statemachine.data.AuthUiState
import com.motorro.statemachine.statemachine.data.ContentUiState
import com.motorro.statemachine.statemachine.ui.LoadingScreen
import org.jetbrains.compose.resources.stringResource

@Composable
fun App(uiState: AppUiState, onGesture: (AppGesture) -> Unit, onFinish: () -> Unit) {
    AndroidTheme {
        when(uiState) {
            is AppUiState.Loading -> LoadingScreen(uiState.message ?: stringResource(Res.string.loading), onGesture)
            is AuthUiState -> AuthScreen(uiState.child) {
                onGesture(AuthGesture(it))
            }
            is ContentUiState -> ContentScreen(uiState, onGesture)
            AppUiState.Terminated -> LaunchedEffect(uiState) {
                onFinish()
            }
        }
    }
}