package com.motorro.cookbook.data.recipes.usecase

import android.util.Log
import androidx.core.net.toUri
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.motorro.cookbook.core.log.Logging
import com.motorro.cookbook.data.recipes.CookbookApi
import com.motorro.cookbook.data.recipes.db.CookbookDao
import com.motorro.cookbook.data.recipes.db.entity.toEntity
import com.motorro.cookbook.data.recipes.usecase.work.AddRecipeWorker
import com.motorro.cookbook.data.recipes.usecase.work.RecipeListWorker
import com.motorro.cookbook.data.recipes.usecase.work.UploadImageWorker
import com.motorro.cookbook.domain.recipes.data.NewRecipe
import com.motorro.cookbook.domain.session.SessionManager
import com.motorro.cookbook.domain.session.requireUserId
import com.motorro.cookbook.model.Image
import com.motorro.cookbook.model.Recipe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named
import kotlin.time.Clock
import kotlin.uuid.Uuid

/**
 * Adds new recipe
 */
internal interface AddRecipeUsecase {
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
 * @param workManager Work manager
 */
internal class AddRecipeUsecaseImpl @Inject constructor(
    private val sessionManager: SessionManager,
    private val cookbookDao: CookbookDao,
    private val cookbookApi: CookbookApi,
    @param:Named("Application") private val scope: CoroutineScope,
    private val clock: Clock,
    private val workManager: WorkManager
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
        val recipe = with(newRecipe) {
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

        // 2. Update server with a recipe
        var work = workManager.beginUniqueWork(
            uniqueWorkName = AddRecipeWorker.getUniqueWorkName(recipeId),
            existingWorkPolicy = ExistingWorkPolicy.REPLACE,
            request = AddRecipeWorker.buildRequest(recipe)
        )

        // 3. Upload recipe image if has any
        if (null != image) {
            work = work.then(
                UploadImageWorker.buildRequest(recipeId, image.url.toUri())
            )
        }

        // 4. Synchronize the list
        work.then(RecipeListWorker.buildOneShot()).enqueue()

        Log.i(TAG, "Created recipe: $recipe")
    }

    companion object {
        private val TAG = AddRecipeUsecase::class.java.simpleName
    }
}