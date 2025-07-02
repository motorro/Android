package com.motorro.cookbook.domain.recipes.data

import com.motorro.cookbook.model.Image
import com.motorro.cookbook.model.RecipeCategory

/**
 * New recipe data
 */
data class NewRecipe(
    val title: String,
    val category: RecipeCategory,
    val description: String,
    val image: Image?
)