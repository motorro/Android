package com.motorro.statemachine.navigation

import androidx.compose.material3.Text
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.compose.composable
import androidx.navigation.createGraph
import com.motorro.statemachine.navigation.content.ContentScreen
import com.motorro.statemachine.navigation.content.ContentViewModel
import com.motorro.statemachine.navigation.login.LoginScreen
import com.motorro.statemachine.navigation.login.LoginViewModel
import kotlinx.serialization.Serializable

@Serializable
data object ContentDestination

@Serializable
data object LoginDestination

@Serializable
data object LogoutDestination

fun NavController.composeAppNavGraph(): NavGraph = createGraph(ContentDestination) {
    composable<ContentDestination> {
        val viewModel: ContentViewModel = viewModel()
        ContentScreen(
            viewModel = viewModel,
            navController = this@composeAppNavGraph
        )
    }

    composable<LoginDestination> {
        val viewModel: LoginViewModel = viewModel()
        LoginScreen(
            viewModel = viewModel,
            navController = this@composeAppNavGraph
        )
    }

    composable<LogoutDestination> {
        Text("Logout")
    }
}