package com.motorro.cookbook.data.recipes.usecase

import android.net.Uri
import android.util.Log
import com.motorro.cookbook.core.log.Logging
import com.motorro.cookbook.data.recipes.CookbookApi
import com.motorro.cookbook.data.recipes.db.CookbookDao
import com.motorro.cookbook.data.recipes.db.entity.toEntity
import com.motorro.cookbook.domain.recipes.data.NewRecipe
import com.motorro.cookbook.domain.session.SessionManager
import com.motorro.cookbook.domain.session.requireUserId
import com.motorro.cookbook.model.Image
import com.motorro.cookbook.model.ImageUpload
import com.motorro.cookbook.model.Recipe
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
 * @param cookbookDao Cookbook DAO
 * @param cookbookApi Cookbook network API
 * @param scope Coroutine scope to run synchronisation
 * @param clock Clock instance
 */
class AddRecipeUsecaseImpl(
    private val sessionManager: SessionManager,
    private val cookbookDao: CookbookDao,
    private val cookbookApi: CookbookApi,
    private val scope: CoroutineScope,
    private val clock: Clock
) : AddRecipeUsecase, Logging {
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
        val userId = sessionManager.requireUserId()
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

        val image = newRecipe.image?.let { Image(it.toString()) }

        // 1. Insert to DB to be ready fast with local image
        recipe.copy(image = image).toEntity(userId).let { (list, data) ->
            cookbookDao.insert(list, data)
        }

        // 2. Update server and get back the finalized data
        recipe = uploadRecipe(recipe).copy(
            image = image?.let { uploadImage(recipeId, Uri.parse(it.url))?.image }
        )

        // 3. Reset local copy to final data
        recipe.toEntity(userId).let { (list, data) ->
            cookbookDao.insert(list, data)
        }

        Log.i(TAG, "Created recipe: $recipe")
    }

    /**
     * Adds recipe record
     */
    private suspend fun uploadRecipe(recipe: Recipe): Recipe = cookbookApi.addRecipe(recipe)
        .onFailure {
            Log.w(TAG, "Failed to create recipe", it)
        }
        .getOrThrow()

    private suspend fun uploadImage(recipeId: Uuid, imageUri: Uri): ImageUpload? = cookbookApi.uploadRecipeImage(recipeId, imageUri)
        .onFailure {
            Log.w(TAG, "Failed to upload image", it)
        }
        .getOrNull()

    companion object {
        private val TAG = AddRecipeUsecase::class.java.simpleName
    }
}