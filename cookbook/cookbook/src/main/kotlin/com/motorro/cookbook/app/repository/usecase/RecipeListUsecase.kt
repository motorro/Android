@file:OptIn(ExperimentalCoroutinesApi::class)

package com.motorro.cookbook.app.repository.usecase

import com.motorro.cookbook.app.data.RecipeListLce
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

/**
 * Recipe list repository use-case
 */
interface RecipeListUsecase {
    /**
     * Returns the list of recipes as a flow with loading state.
     */
    val recipes: Flow<RecipeListLce>

    /**
     * Reloads list data from server
     */
    fun synchronize()
}

/**
 * Retrieves recipes
 * @param sessionManager Session manager
 * @param cookbookApi Cookbook network API
 * @param scope Coroutine scope to run synchronisation
 */
class RecipeListUsecaseImpl(
    private val sessionManager: SessionManager,
    private val cookbookApi: CookbookApi,
    private val scope: CoroutineScope
) : RecipeListUsecase {

    /**
     * Synchronization flow
     */
    private val sync = MutableSharedFlow<Unit>()

    override val recipes: Flow<RecipeListLce> =  sync.onStart { emit(Unit) }.transformLatest {
        emit(LceState.Loading())
        emitAll(sessionManager.withUserId {
            flowOf(cookbookApi.getRecipeList().fold(
                { LceState.Content(it) },
                { LceState.Error(it.toCookbookError()) }
            ))
        })
    }

    override fun synchronize() {
        scope.launch{
            sync.emit(Unit)
        }
    }
}