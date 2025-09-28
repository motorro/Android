package com.motorro.cookbook.recipelist

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.motorro.cookbook.appcore.navigation.Destination
import com.motorro.cookbook.recipelist.ui.RecipeListScreen

fun NavGraphBuilder.recipeListGraph(navController: NavController) {
    composable<Destination.RecipeListDestination> {
        val model: RecipeListViewModel = hiltViewModel()

        RecipeListScreen(
            viewState = model.viewState.collectAsStateWithLifecycle().value,
            onGesture = model::process,
            onLogin = {
                navController.navigate(Destination.LoginDestination)
            },
            onTerminated = { navController.popBackStack() }
        )
    }
}