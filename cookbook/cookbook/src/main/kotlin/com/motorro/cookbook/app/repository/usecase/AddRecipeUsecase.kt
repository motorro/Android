package com.motorro.cookbook.app.repository.usecase

import android.net.Uri
import android.util.Log
import com.motorro.cookbook.app.data.NewRecipe
import com.motorro.cookbook.app.repository.CookbookApi
import com.motorro.cookbook.app.session.SessionManager
import com.motorro.cookbook.app.session.requireUserId
import com.motorro.cookbook.data.ImageUpload
import com.motorro.cookbook.data.Recipe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.uuid.Uuid

/**
 * Adds new recipe
 */
interface AddRecipeUsecase {
    /**
     * Adds new recipe
     * @param recipe Recipe to add
     */
    operator fun invoke(recipe: NewRecipe)
}

/**
 * Add recipe implementation
 * @param sessionManager Session manager
 * @param cookbookApi Cookbook network API
 * @param scope Coroutine scope to run synchronisation
 * @param clock Clock instance
 */
class AddRecipeUsecaseImpl(
    private val sessionManager: SessionManager,
    private val cookbookApi: CookbookApi,
    private val scope: CoroutineScope,
    private val clock: Clock
) : AddRecipeUsecase {
    override fun invoke(recipe: NewRecipe) {
        scope.launch {
            try {
                doInvoke(recipe)
            } catch (e: Throwable) {
                Log.w(TAG, "Failed to create recipe", e)
            }
        }
    }

    /**
     * Runs creation usecase
     */
    private suspend fun doInvoke(newRecipe: NewRecipe) {
        sessionManager.requireUserId()
        val recipeId = Uuid.random()

        // Common recipe data
        var recipe = with(newRecipe) {
            Recipe(
                id = recipeId,
                title = title,
                category = category,
                image = null,
                description = description,
                dateTimeCreated = clock.now()
            )
        }

        recipe = createRecipe(recipe).copy(
            image = newRecipe.image?.let { uploadImage(recipeId, it)?.image }
        )

        Log.i(TAG, "Created recipe: $recipe")
    }

    /**
     * Adds recipe record
     */
    private suspend fun createRecipe(recipe: Recipe): Recipe = cookbookApi.addRecipe(recipe)
        .onFailure {
            Log.w(TAG, "Failed to create recipe", it)
        }
        .getOrThrow()

    private suspend fun uploadImage(recipeId: Uuid, image: Uri): ImageUpload? = cookbookApi.uploadRecipeImage(recipeId, image)
        .onFailure {
            Log.w(TAG, "Failed to upload image", it)
        }
        .getOrNull()

    companion object {
        private val TAG = AddRecipeUsecase::class.java.simpleName
    }
}