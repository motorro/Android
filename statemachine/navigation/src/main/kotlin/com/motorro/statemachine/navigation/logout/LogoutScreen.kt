package com.motorro.statemachine.navigation.logout

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.motorro.statemachine.common.data.gesture.LoadingGesture
import com.motorro.statemachine.common.data.ui.LoadingUiState
import com.motorro.statemachine.common.ui.LoadingScreen
import com.motorro.statemachine.common.ui.ScreenScaffold
import com.motorro.statemachine.navigation.ContentDestination
import com.motorro.statemachine.navigation.R

@Composable
fun LogoutScreen(
    viewModel: LogoutViewModel,
    navController: NavController
) {
    // Navigation
    val onContent = { navController.navigate(ContentDestination) }

    ScreenScaffold(title = { Text(stringResource(R.string.title_logout)) }) { paddingValues ->
        LoadingScreen(LoadingUiState(stringResource(R.string.logging_out)), Modifier.padding(paddingValues)) {
            when(it) {
                LoadingGesture.Back ->  { }
            }
        }
    }

    LaunchedEffect(viewModel) {
        viewModel.navigationEvents.collect {
            when(it) {
                LogoutNavigationEvent.NavigateToContent -> onContent()
            }
        }
    }
}