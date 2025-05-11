@file:OptIn(ExperimentalCoroutinesApi::class)

package com.motorro.cookbook.app.repository.usecase

import com.motorro.cookbook.app.data.CookbookError
import com.motorro.cookbook.app.data.RecipeListLce
import com.motorro.cookbook.app.data.toCookbookError
import com.motorro.cookbook.app.db.CookbookDao
import com.motorro.cookbook.app.db.entity.toDomain
import com.motorro.cookbook.app.db.entity.toEntity
import com.motorro.cookbook.app.repository.CookbookApi
import com.motorro.cookbook.app.session.SessionManager
import com.motorro.cookbook.app.session.withUserId
import com.motorro.cookbook.data.ListRecipe
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
 * @param cookbookDao Cookbook database DAO
 * @param cookbookApi Cookbook network API
 * @param scope Coroutine scope to run synchronisation
 */
class RecipeListUsecaseImpl(
    private val sessionManager: SessionManager,
    private val cookbookDao: CookbookDao,
    private val cookbookApi: CookbookApi,
    private val scope: CoroutineScope
) : RecipeListUsecase {

    // Network operation job
    private var syncJob: Job? = null

    // Network operation state
    private val network = MutableStateFlow<LceState<*, CookbookError>>(LceState.Loading(Unit))

    /**
     * Returns the list of recipes as a flow with loading state.
     */
    override val recipes: Flow<RecipeListLce> = sessionManager.withUserId { userId ->
        // Start network sync on subscribe
        synchronize()

        // Take database data and merge it with network state
        // Thus even if we have a network error, we will get some cached data
        combine(
            flow = cookbookDao.list(userId.id).map { list -> list.map { it.toDomain() } },
            flow2 = network,
            transform = { fromDb, networkState -> networkState.replaceData(fromDb.takeIf { it.isNotEmpty() }) }
        )
    }

    /**
     * Reloads list data from server
     */
    override fun synchronize() {
        syncJob?.cancel()
        syncJob = scope.launch {
            loadFromNetwork()
                .onEachData { (userId, recipes) ->
                    cookbookDao.insertList(recipes.map { it.toEntity(userId) })
                }
                .collect(network)
        }
    }

    /**
     * Loads network data
     */
    private fun loadFromNetwork(): Flow<LceState<Pair<UserId, List<ListRecipe>>, CookbookError>> = flow {
        emit(LceState.Loading())
        emitAll(sessionManager.withUserId { userId ->
            flowOf(cookbookApi.getRecipeList().fold(
                { LceState.Content(userId to it) },
                { LceState.Error(it.toCookbookError()) }
            ))
        })
    }
}
