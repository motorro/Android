package com.motorro.cookbook.app.repository.usecase

import android.util.Log
import com.motorro.cookbook.app.repository.CookbookApi
import com.motorro.cookbook.app.session.SessionManager
import com.motorro.cookbook.app.session.requireUserId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlin.uuid.Uuid

/**
 * Deletes recipe
 */
interface DeleteRecipeUsecase {
    /**
     * Deletes recipe
     * @param id Recipe ID to delete
     */
    operator fun invoke(id: Uuid)
}

/**
 * Delete recipe implementation
 * @param sessionManager Session manager
 * @param cookbookApi Cookbook network API
 * @param scope Coroutine scope to run synchronisation
 */
class DeleteRecipeUsecaseImpl(
    private val sessionManager: SessionManager,
    private val cookbookApi: CookbookApi,
    private val scope: CoroutineScope
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
        sessionManager.requireUserId()
        deleteOnServer(recipeId)
        Log.i(TAG, "Deleted recipe: $recipeId")
    }

    /**
     * Deletes recipe
     */
    private suspend fun deleteOnServer(recipeId: Uuid) = cookbookApi.deleteRecipe(recipeId)
        .onFailure {
            Log.w(TAG, "Failed to delete recipe", it)
        }
        .getOrThrow()

    companion object {
        private val TAG = DeleteRecipeUsecase::class.java.simpleName
    }
}
