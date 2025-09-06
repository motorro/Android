package com.motorro.statemachine.navigation.content

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.motorro.statemachine.common.data.gesture.ContentGesture
import com.motorro.statemachine.common.data.gesture.LoadingGesture
import com.motorro.statemachine.common.ui.ContentScreen
import com.motorro.statemachine.common.ui.LoadingScreen
import com.motorro.statemachine.common.ui.ScreenScaffold
import com.motorro.statemachine.navigation.LoginDestination
import com.motorro.statemachine.navigation.LogoutDestination
import com.motorro.statemachine.navigation.R

@Composable
fun ContentScreen(
    viewModel: ContentViewModel,
    navController: NavController
) {
    // Navigation
    val onBack = { navController.popBackStack() }
    val onLogin = { navController.navigate(LoginDestination) }
    val onLogout = { navController.navigate(LogoutDestination) }

    val state = viewModel.uiState.collectAsStateWithLifecycle()

    ScreenScaffold(title = { Text(stringResource(R.string.title_content)) }) { paddingValues ->
        val modifier = Modifier.padding(paddingValues)
        when(val s = state.value) {
            is ContentScreenState.Content -> ContentScreen(s.state, modifier) {
                when(it) {
                    ContentGesture.Back -> onBack()
                    ContentGesture.Logout -> viewModel.logout()
                }
            }
            is ContentScreenState.Loading -> LoadingScreen(s.state, modifier) {
                when(it) {
                    LoadingGesture.Back -> onBack()
                }
            }
        }
    }

    LaunchedEffect(viewModel) {
        viewModel.navigationEvents.collect {
            when(it) {
                ContentNavigationEvent.NavigateToLogin -> onLogin()
                ContentNavigationEvent.NavigateToLogout -> onLogout()
            }
        }
    }
}