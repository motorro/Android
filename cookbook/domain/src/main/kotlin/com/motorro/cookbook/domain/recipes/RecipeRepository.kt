package com.motorro.cookbook.domain.recipes

import com.motorro.cookbook.domain.recipes.data.NewRecipe
import com.motorro.cookbook.domain.recipes.data.RecipeLce
import com.motorro.cookbook.domain.recipes.data.RecipeListLce
import com.motorro.cookbook.model.RecipeCategory
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

/**
 * Provides access to the recipes.
 */
interface RecipeRepository {
    /**
     * Returns the list of recipes as a flow with loading state.
     */
    val recipes: Flow<RecipeListLce>

    /**
     * Reloads list data from server
     */
    fun synchronizeList()

    /**
     * Returns the recipe loading state flow with the specified ID.
     * @param id Recipe ID
     */
    fun getRecipe(id: Uuid): Flow<RecipeLce>

    /**
     * Reloads recipe data from server
     * @param id Recipe ID
     */
    fun synchronizeRecipe(id: Uuid)

    /**
     * A list of categories
     */
    val categories: Flow<List<RecipeCategory>>

    /**
     * Adds new recipe
     * @param recipe Recipe to add
     */
    fun addRecipe(recipe: NewRecipe)

    /**
     * Deletes recipe
     * @param id Recipe ID to delete
     */
    fun deleteRecipe(id: Uuid)
}

