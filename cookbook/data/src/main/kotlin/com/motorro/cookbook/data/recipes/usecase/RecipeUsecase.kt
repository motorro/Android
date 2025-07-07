@file:OptIn(ExperimentalCoroutinesApi::class)

package com.motorro.cookbook.data.recipes.usecase

import com.motorro.cookbook.core.error.CoreException
import com.motorro.cookbook.core.error.toCore
import com.motorro.cookbook.core.lce.LceState
import com.motorro.cookbook.core.lce.onEachData
import com.motorro.cookbook.core.lce.replaceData
import com.motorro.cookbook.data.recipes.CookbookApi
import com.motorro.cookbook.data.recipes.db.CookbookDao
import com.motorro.cookbook.data.recipes.db.entity.toDomain
import com.motorro.cookbook.data.recipes.db.entity.toEntity
import com.motorro.cookbook.domain.recipes.data.RecipeLce
import com.motorro.cookbook.domain.session.SessionManager
import com.motorro.cookbook.domain.session.withUserId
import com.motorro.cookbook.model.Recipe
import com.motorro.cookbook.model.UserId
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Named
import kotlin.uuid.Uuid

/**
 * Recipe repository use-case
 * Recipe ID is bound externally
 */
internal interface RecipeUsecase {
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
    @AssistedFactory
    interface Factory {
        /**
         * Creates use-case for given ID
         * @param recipeId Recipe ID to bind
         */
        operator fun invoke(recipeId: Uuid): RecipeUsecaseImpl
    }
}

/**
 * Retrieves recipe
 * @param recipeId Bound recipe ID
 * @param sessionManager Session manager
 * @param cookbookDao Cookbook DAO
 * @param cookbookApi Cookbook network API
 * @param scope Coroutine scope to run synchronisation
 */
internal class RecipeUsecaseImpl @AssistedInject constructor(
    @Assisted private val recipeId: Uuid,
    private val sessionManager: SessionManager,
    private val cookbookDao: CookbookDao,
    private val cookbookApi: CookbookApi,
    @param:Named("Application") private val scope: CoroutineScope,
) : RecipeUsecase {

    // Network operation job
    private var syncJob: Job? = null

    // Network operation state
    private val network = MutableStateFlow<LceState<*, CoreException>>(LceState.Loading(Unit))

    /**
     * Returns the list of recipes as a flow with loading state.
     */
    override val recipe: Flow<RecipeLce> = sessionManager.withUserId { userId ->
        // Start network sync on subscribe
        synchronize()

        // Take database data and merge it with network state
        // Thus even if we have a network error, we will get some cached data
        combine(
            flow = cookbookDao.recipe(userId.id, recipeId.toString()).map { it.firstOrNull()?.toDomain() },
            flow2 = network,
            transform = { fromDb, networkState -> networkState.replaceData(fromDb) }
        )
    }

    /**
     * Reloads list data from server
     */
    override fun synchronize() {
        syncJob?.cancel()
        syncJob = scope.launch {
            loadFromNetwork(recipeId)
                .onEachData { (userId, recipe) ->
                    recipe.toEntity(userId).let { (list, data) ->
                        cookbookDao.insert(list, data)
                    }
                }
                .collect(network)
        }
    }

    /**
     * Loads network data
     */
    private fun loadFromNetwork(recipeId: Uuid): Flow<LceState<Pair<UserId, Recipe>, CoreException>> = flow {
        emit(LceState.Loading())
        emitAll(sessionManager.withUserId { userId ->
            flowOf(cookbookApi.getRecipe(recipeId).fold(
                { LceState.Content(userId to it) },
                { LceState.Error(it.toCore()) }
            ))
        })
    }
}
