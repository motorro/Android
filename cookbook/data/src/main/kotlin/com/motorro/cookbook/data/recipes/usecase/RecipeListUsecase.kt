@file:OptIn(ExperimentalCoroutinesApi::class)

package com.motorro.cookbook.data.recipes.usecase

import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.motorro.cookbook.core.error.CoreException
import com.motorro.cookbook.core.error.toCore
import com.motorro.cookbook.core.lce.LceState
import com.motorro.cookbook.data.recipes.CookbookApi
import com.motorro.cookbook.data.recipes.db.CookbookDao
import com.motorro.cookbook.data.recipes.db.entity.toDomain
import com.motorro.cookbook.data.recipes.usecase.work.RecipeListWorker
import com.motorro.cookbook.data.work.WorkState
import com.motorro.cookbook.data.work.getCombinedWorkInfo
import com.motorro.cookbook.data.work.toLce
import com.motorro.cookbook.domain.recipes.data.RecipeListLce
import com.motorro.cookbook.domain.session.SessionManager
import com.motorro.cookbook.domain.session.withUserId
import com.motorro.cookbook.model.ListRecipe
import com.motorro.cookbook.model.UserId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject
import javax.inject.Named

/**
 * Recipe list repository use-case
 */
internal interface RecipeListUsecase {
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
internal class RecipeListUsecaseImpl @Inject constructor(
    private val sessionManager: SessionManager,
    private val workManager: WorkManager,
    private val cookbookDao: CookbookDao,
    private val cookbookApi: CookbookApi,
    @param:Named("Application") private val scope: CoroutineScope,
) : RecipeListUsecase {

    /**
     * Returns the list of recipes as a flow with loading state.
     */
    override val recipes: Flow<RecipeListLce> = sessionManager.withUserId { userId ->
        // Work status
        val workStatus = workManager.getWorkInfosByTagFlow(RecipeListWorker.TAG).map { it.getCombinedWorkInfo() }

        // Take database data and merge it with network state
        // Thus even if we have a network error, we will get some cached data
        combine(
            flow = cookbookDao.list(userId.id).map { list -> list.map { it.toDomain() } },
            flow2 = workStatus.onStart { emit(WorkState.Idle) },
            transform = { fromDb, workState ->
                workState.toLce(fromDb)
            }
        )
    }

    /**
     * Reloads list data from server
     */
    override fun synchronize() {
        workManager.enqueueUniqueWork(
            uniqueWorkName = RecipeListWorker.UNIQUE_ONE_SHOT_NAME,
            existingWorkPolicy = ExistingWorkPolicy.KEEP,
            request = RecipeListWorker.buildOneShot()
        )
    }

    /**
     * Loads network data
     */
    private fun loadFromNetwork(): Flow<LceState<Pair<UserId, List<ListRecipe>>, CoreException>> = flow {
        emit(LceState.Loading())
        emitAll(sessionManager.withUserId { userId ->
            flowOf(cookbookApi.getRecipeList().fold(
                { LceState.Content(userId to it) },
                { LceState.Error(it.toCore()) }
            ))
        })
    }
}

