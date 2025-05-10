package com.motorro.cookbook.app.repository

import com.motorro.cookbook.app.data.NewRecipe
import com.motorro.cookbook.app.data.RecipeLce
import com.motorro.cookbook.app.data.RecipeListLce
import com.motorro.cookbook.app.repository.usecase.RecipeListUsecase
import com.motorro.cookbook.data.RecipeCategory
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

/**
 * Combines use-cases to a single repository
 */
class RecipeRepositoryImpl(
    private val recipeListUsecase: RecipeListUsecase
) : RecipeRepository {

    override val recipes: Flow<RecipeListLce> get() = recipeListUsecase.recipes

    override fun synchronizeList() {
        recipeListUsecase.synchronize()
    }

    override fun getRecipe(id: Uuid): Flow<RecipeLce> {
        TODO("Not yet implemented")
    }

    override fun synchronizeRecipe(id: Uuid) {
        TODO("Not yet implemented")
    }

    override val categories: Flow<List<RecipeCategory>>
        get() = TODO("Not yet implemented")


    override fun addRecipe(recipe: NewRecipe) {
        TODO("Not yet implemented")
    }

    override fun deleteRecipe(id: Uuid) {
        TODO("Not yet implemented")
    }
}