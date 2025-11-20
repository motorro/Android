package com.motorro.cookbook.data.recipes.usecase

import android.util.Log
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.motorro.cookbook.data.recipes.CookbookApi
import com.motorro.cookbook.data.recipes.db.CookbookDao
import com.motorro.cookbook.data.recipes.usecase.work.DeleteRecipeWorker
import com.motorro.cookbook.data.recipes.usecase.work.RecipeListWorker
import com.motorro.cookbook.domain.session.SessionManager
import com.motorro.cookbook.domain.session.requireUserId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named
import kotlin.uuid.Uuid

/**
 * Deletes recipe
 */
internal interface DeleteRecipeUsecase {
    /**
     * Deletes recipe
     * @param id Recipe ID to delete
     */
    operator fun invoke(id: Uuid)
}

/**
 * Delete recipe implementation. Deletes locally, then synchs to server
 * @param sessionManager Session manager
 * @param cookbookDao Cookbook database access object
 * @param cookbookApi Cookbook network API
 * @param scope Coroutine scope to run synchronisation
 * @param workManager Work manager
 */
internal class DeleteRecipeUsecaseImpl @Inject constructor(
    private val sessionManager: SessionManager,
    private val cookbookDao: CookbookDao,
    private val cookbookApi: CookbookApi,
    @param:Named("Application") private val scope: CoroutineScope,
    private val workManager: WorkManager
) : DeleteRecipeUsecase {

    override fun invoke(id: Uuid) {
        scope.launch {
            try {
                doInvoke(id)
            } catch (e: Throwable) {
                currentCoroutineContext().ensureActive()
                Log.w(TAG, "Failed to delete recipe", e)
            }
        }
    }

    /**
     * Runs creation usecase
     */
    private suspend fun doInvoke(recipeId: Uuid) {
        cookbookDao.delete(sessionManager.requireUserId().id, recipeId.toString())
        deleteOnServer(recipeId)
        Log.i(TAG, "Deleted recipe: $recipeId")
    }

    /**
     * Deletes recipe
     */
    private fun deleteOnServer(recipeId: Uuid) {
        workManager
            .beginUniqueWork(
                uniqueWorkName = DeleteRecipeWorker.getUniqueWorkName(recipeId),
                existingWorkPolicy = ExistingWorkPolicy.KEEP,
                request = DeleteRecipeWorker.buildRequest(recipeId)
            )
            .then(RecipeListWorker.buildOneShot())
            .enqueue()
    }

    companion object {
        private val TAG = DeleteRecipeUsecase::class.java.simpleName
    }
}
