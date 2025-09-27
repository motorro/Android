package com.motorro.cookbook.recipe

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.motorro.cookbook.appcore.navigation.Destination
import kotlin.uuid.Uuid

/**
 * Login screen navigation graph
 */
fun NavGraphBuilder.recipeGraph(navController: NavController) {
    composable<Destination.RecipeDestination> { backStackEntry ->
        val model: RecipeViewModel = hiltViewModel<RecipeViewModel, RecipeViewModel.Factory>(
            creationCallback = {
                val route: Destination.RecipeDestination = backStackEntry.toRoute()
                val idString = requireNotNull(route.id) {
                    "Recipe id is required"
                }
                val idUuid = Uuid.parse(idString)

                it.create(idUuid)
            }
        )

        RecipeScreen(
            viewState = model.viewState.collectAsStateWithLifecycle().value,
            onGesture = model::process,
            onTerminated = { navController.popBackStack() },
            onLogin = {
                navController.navigate(Destination.LoginDestination)
            }
        )
    }
}