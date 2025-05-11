package com.motorro.cookbook.app.repository.usecase

import com.motorro.cookbook.app.db.CookbookDao
import com.motorro.cookbook.app.session.SessionManager
import com.motorro.cookbook.app.session.data.Session
import com.motorro.cookbook.data.RecipeCategory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

/**
 * Exports the list of categories
 */
interface CategoriesUsecase {
    /**
     * Categories list flow
     */
    val categories: Flow<List<RecipeCategory>>
}

/**
 * Categories usecase implementation that uses local database to fetch the
 * list of existing categories
 * @param sessionManager Session manager
 * @param cookbookDao Cookbook database DAO
 */
@OptIn(ExperimentalCoroutinesApi::class)
class CategoriesUsecaseImpl(
    sessionManager: SessionManager,
    cookbookDao: CookbookDao,
) : CategoriesUsecase {

    /**
     * Takes all categories from DB
     */
    override val categories: Flow<List<RecipeCategory>> = sessionManager.session.flatMapLatest { session ->
        when(session) {
            Session.Loading -> emptyFlow()
            Session.None -> flowOf(emptyList())
            is Session.Active -> cookbookDao.categories(session.profile.id.id)
        }
    }
}