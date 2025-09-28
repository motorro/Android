package com.motorro.cookbook.recipe.data

import com.motorro.cookbook.appcore.navigation.auth.AuthGesture

/**
 * Gestures used by the Recipe screen flow
 */
sealed class RecipeGesture {
    data object Back : RecipeGesture()
    data object DismissError : RecipeGesture()
    data object Delete : RecipeGesture()
    data object ConfirmDelete : RecipeGesture()
    data object CancelDelete : RecipeGesture()

    /**
     * Proxies authentication flow
     */
    data class Auth(val child: AuthGesture) : RecipeGesture()
}