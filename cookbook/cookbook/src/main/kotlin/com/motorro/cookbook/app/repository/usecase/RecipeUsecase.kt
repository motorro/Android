@file:OptIn(ExperimentalCoroutinesApi::class)

package com.motorro.cookbook.app.repository.usecase

import com.motorro.cookbook.app.data.CookbookError
import com.motorro.cookbook.app.data.RecipeLce
import com.motorro.cookbook.app.data.toCookbookError
import com.motorro.cookbook.app.db.CookbookDao
import com.motorro.cookbook.app.db.entity.toDomain
import com.motorro.cookbook.app.db.entity.toEntity
import com.motorro.cookbook.app.repository.CookbookApi
import com.motorro.cookbook.app.session.SessionManager
import com.motorro.cookbook.app.session.withUserId
import com.motorro.cookbook.data.Recipe
import com.motorro.cookbook.data.UserId
import com.motorro.core.lce.LceState
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
 * @param cookbookDao Cookbook DAO
 * @param cookbookApi Cookbook network API
 * @param scope Coroutine scope to run synchronisation
 */
class RecipeUsecaseImpl(
    private val recipeId: Uuid,
    private val sessionManager: SessionManager,
    private val cookbookDao: CookbookDao,
    private val cookbookApi: CookbookApi,
    private val scope: CoroutineScope
) : RecipeUsecase {

    // Network operation job
    private var syncJob: Job? = null

    // Network operation state
    private val network = MutableStateFlow<LceState<*, CookbookError>>(LceState.Loading(Unit))

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
    private fun loadFromNetwork(recipeId: Uuid): Flow<LceState<Pair<UserId, Recipe>, CookbookError>> = flow {
        emit(LceState.Loading())
        emitAll(sessionManager.withUserId { userId ->
            flowOf(cookbookApi.getRecipe(recipeId).fold(
                { LceState.Content(userId to it) },
                { LceState.Error(it.toCookbookError()) }
            ))
        })
    }
}
