package com.motorro.cookbook.addrecipe

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.motorro.cookbook.appcore.navigation.Destination

/**
 * Add recipe screen navigation graph
 */
fun NavGraphBuilder.addRecipeGraph(navController: NavController) {
    composable<Destination.AddRecipeDestination> {
        val model: AddRecipeFragmentViewModel = hiltViewModel()

        AddRecipeScreen(
            viewState = model.viewState.collectAsStateWithLifecycle().value,
            onGesture = model::process,
            onTerminate = { navController.popBackStack() }
        )
    }
}