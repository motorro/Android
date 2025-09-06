package com.motorro.statemachine.navigation.login

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.motorro.statemachine.common.data.gesture.ErrorGesture
import com.motorro.statemachine.common.data.gesture.LoadingGesture
import com.motorro.statemachine.common.data.gesture.LoginGesture
import com.motorro.statemachine.common.data.ui.LoginUiState
import com.motorro.statemachine.common.ui.ErrorScreen
import com.motorro.statemachine.common.ui.LoadingScreen
import com.motorro.statemachine.common.ui.LoginFormScreen
import com.motorro.statemachine.common.ui.ScreenScaffold
import com.motorro.statemachine.navigation.ContentDestination
import com.motorro.statemachine.navigation.R

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    navController: NavController,
    onTerminate: () -> Unit
) {
    // Navigation
    val onBack = { navController.popBackStack() }
    val onContent = { navController.navigate(ContentDestination) }

    val state = viewModel.uiState.collectAsStateWithLifecycle()

    ScreenScaffold(title = { Text(stringResource(R.string.title_login)) }) { paddingValues ->
        val modifier = Modifier.padding(paddingValues)
        when(val s = state.value) {
            is LoginUiState.Form -> LoginFormScreen(s, modifier) {
                when(it) {
                    LoginGesture.Action -> {
                        viewModel.login()
                    }
                    LoginGesture.Back -> onBack()
                    is LoginGesture.UsernameChanged -> viewModel.setUsername(it.value)
                    is LoginGesture.PasswordChanged -> viewModel.setPassword(it.value)
                }
            }
            is LoginUiState.Error -> ErrorScreen(s.state, modifier) {
                // Should we place any logic here?
                // Or move it to the view-model?
                when(it) {
                    ErrorGesture.Action -> if (s.state.canRetry) {
                        viewModel.retry()
                    } else {
                        onTerminate()
                    }
                    ErrorGesture.Back -> onBack()
                }
            }
            is LoginUiState.Loading -> LoadingScreen(s.state, modifier) {
                when(it) {
                    LoadingGesture.Back -> onBack()
                }
            }
        }
    }

    LaunchedEffect(viewModel) {
        viewModel.navigationEvents.collect {
            when(it) {
                LoginNavigationEvent.NavigateToContent -> onContent()
            }
        }
    }
}