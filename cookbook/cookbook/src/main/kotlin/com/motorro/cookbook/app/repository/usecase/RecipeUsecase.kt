@file:OptIn(ExperimentalCoroutinesApi::class)

package com.motorro.cookbook.app.repository.usecase

import com.motorro.cookbook.app.data.RecipeLce
import com.motorro.cookbook.app.data.toCookbookError
import com.motorro.cookbook.app.repository.CookbookApi
import com.motorro.cookbook.app.session.SessionManager
import com.motorro.cookbook.app.session.withUserId
import com.motorro.core.lce.LceState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.launch
import kotlin.uuid.Uuid

/**
 * Recipe repository use-case
 * Recipe ID is bound externally
 */
interface RecipeUsecase {
    /**
     * Returns the recipe loading state flow with the specified ID.
     */
    val recipe: Flow<RecipeLce>

    /**
     * Reloads data from server
     */
    fun synchronize()

    /**
     * Creates use-case for given ID
     */
    interface Factory {
        /**
         * Creates use-case for given ID
         * @param recipeId Recipe ID to bind
         */
        operator fun invoke(recipeId: Uuid): RecipeUsecase
    }
}

/**
 * Retrieves recipe
 * @param recipeId Bound recipe ID
 * @param sessionManager Session manager
 * @param cookbookApi Cookbook network API
 * @param scope Coroutine scope to run synchronisation
 */
class RecipeUsecaseImpl(
    private val recipeId: Uuid,
    private val sessionManager: SessionManager,
    private val cookbookApi: CookbookApi,
    private val scope: CoroutineScope
) : RecipeUsecase {

    /**
     * Synchronization flow
     */
    private val sync = MutableSharedFlow<Unit>()

    override val recipe: Flow<RecipeLce> = sync.onStart { emit(Unit) }.transformLatest {
        emit(LceState.Loading())
        emitAll(sessionManager.withUserId {
            flowOf(cookbookApi.getRecipe(recipeId).fold(
                { LceState.Content(it) },
                { LceState.Error(it.toCookbookError()) }
            ))
        })
    }

    override fun synchronize() {
        scope.launch {
            sync.emit(Unit)
        }
    }
}