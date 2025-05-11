package com.motorro.cookbook.app.repository

import com.motorro.cookbook.app.data.NewRecipe
import com.motorro.cookbook.app.data.RecipeLce
import com.motorro.cookbook.app.data.RecipeListLce
import com.motorro.cookbook.app.getWeakOrPut
import com.motorro.cookbook.app.repository.usecase.AddRecipeUsecase
import com.motorro.cookbook.app.repository.usecase.CategoriesUsecase
import com.motorro.cookbook.app.repository.usecase.DeleteRecipeUsecase
import com.motorro.cookbook.app.repository.usecase.RecipeListUsecase
import com.motorro.cookbook.app.repository.usecase.RecipeUsecase
import com.motorro.cookbook.data.RecipeCategory
import kotlinx.coroutines.flow.Flow
import java.lang.ref.WeakReference
import kotlin.uuid.Uuid

/**
 * Combines use-cases to a single repository
 */
class RecipeRepositoryImpl(
    private val recipeListUsecase: RecipeListUsecase,
    private val createRecipeUsecase: RecipeUsecase.Factory,
    private val categoriesUsecase: CategoriesUsecase,
    private val addRecipeUsecase: AddRecipeUsecase,
    private val deleteRecipeUsecase: DeleteRecipeUsecase
) : RecipeRepository {

    override val recipes: Flow<RecipeListLce> get() = recipeListUsecase.recipes

    override fun synchronizeList() {
        recipeListUsecase.synchronize()
    }

    // Holds weak references to created recipe-use-cases
    // They will go away as soon as they are not used anymore
    private val recipeUsecases = mutableMapOf<Uuid, WeakReference<RecipeUsecase>>()

    override fun getRecipe(id: Uuid): Flow<RecipeLce> = recipeUsecases
        .getWeakOrPut(id) {
            createRecipeUsecase(id)
        }
        .recipe

    override fun synchronizeRecipe(id: Uuid) {
        recipeUsecases
            .getWeakOrPut(id) { createRecipeUsecase(id) }
            .synchronize()
    }

    override val categories: Flow<List<RecipeCategory>> get() = categoriesUsecase.categories

    override fun addRecipe(recipe: NewRecipe) = addRecipeUsecase(recipe)

    override fun deleteRecipe(id: Uuid) = deleteRecipeUsecase(id)
}