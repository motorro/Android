package com.motorro.statemachine.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.compose.composable
import androidx.navigation.createGraph
import com.motorro.statemachine.navigation.content.ContentScreen
import com.motorro.statemachine.navigation.content.ContentViewModel
import com.motorro.statemachine.navigation.login.LoginScreen
import com.motorro.statemachine.navigation.login.LoginViewModel
import com.motorro.statemachine.navigation.logout.LogoutScreen
import com.motorro.statemachine.navigation.logout.LogoutViewModel
import kotlinx.serialization.Serializable

@Serializable
data object ContentDestination

@Serializable
data object LoginDestination

@Serializable
data object LogoutDestination

fun NavController.composeAppNavGraph(onTerminate: () -> Unit): NavGraph = createGraph(ContentDestination) {
    composable<ContentDestination> {
        val viewModel: ContentViewModel = viewModel()
        ContentScreen(
            viewModel = viewModel,
            navController = this@composeAppNavGraph,
            onTerminate = onTerminate
        )
    }

    composable<LoginDestination> {
        val viewModel: LoginViewModel = viewModel()
        LoginScreen(
            viewModel = viewModel,
            navController = this@composeAppNavGraph,
            onTerminate = onTerminate
        )
    }

    composable<LogoutDestination> {
        val viewModel: LogoutViewModel = viewModel()
        LogoutScreen(
            viewModel = viewModel,
            navController = this@composeAppNavGraph
        )
    }
}