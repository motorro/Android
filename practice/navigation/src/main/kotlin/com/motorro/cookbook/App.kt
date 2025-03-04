package com.motorro.cookbook

import android.app.Application
import com.motorro.cookbook.data.RecipeRepository
import com.motorro.cookbook.data.cookbook

/**
 * The application. Provides access to dependencies.
 */
class App : Application() {
    /**
     * The recipe repository.
     */
    val recipeRepository: RecipeRepository by lazy {
        RecipeRepository.Impl(recipes = cookbook)
    }
}