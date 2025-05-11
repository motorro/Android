package com.motorro.cookbook.app.repository.usecase

import com.motorro.cookbook.data.RecipeCategory
import kotlinx.coroutines.flow.Flow
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
 * Categories usecase implementation
 */
class CategoriesUsecaseImpl : CategoriesUsecase {
    // No categories so far
    override val categories: Flow<List<RecipeCategory>> = flowOf(emptyList())
}