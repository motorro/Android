package com.motorro.cookbook.login

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.motorro.cookbook.appcore.navigation.Destination


/**
 * Login screen navigation graph
 */
fun NavGraphBuilder.loginGraph(navController: NavController) {
    composable<Destination.LoginDestination> {
        val model: LoginViewModel = hiltViewModel()

        LoginScreen(
            state = model.viewState.collectAsStateWithLifecycle().value,
            onGesture = model::process,
            onComplete = { navController.popBackStack() }
        )
    }
}