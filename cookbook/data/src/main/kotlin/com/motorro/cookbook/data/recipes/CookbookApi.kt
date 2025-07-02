package com.motorro.cookbook.data.recipes

import android.net.Uri
import com.motorro.cookbook.model.ImageUpload
import com.motorro.cookbook.model.ListRecipe
import com.motorro.cookbook.model.Recipe
import kotlin.uuid.Uuid

/**
 * Cookbook network API
 */
interface CookbookApi {
    /**
     * Load a list of recipes
     */
    suspend fun getRecipeList(): Result<List<ListRecipe>>

    /**
     * Load a recipe
     * @param id Recipe ID
     */
    suspend fun getRecipe(id: Uuid): Result<Recipe>

    /**
     * Adds recipe to cookbook
     * @param recipe Recipe to add
     */
    suspend fun addRecipe(recipe: Recipe): Result<Recipe>

    /**
     * Uploads recipe image
     * @param recipeId Recipe ID
     * @param imageUri Image URI
     */
    suspend fun uploadRecipeImage(recipeId: Uuid, imageUri: Uri): Result<ImageUpload>

    /**
     * Deletes recipe
     * @param recipeId Recipe ID
     */
    suspend fun deleteRecipe(recipeId: Uuid): Result<Unit>
}
