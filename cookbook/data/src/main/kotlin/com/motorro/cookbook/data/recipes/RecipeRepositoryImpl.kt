package com.motorro.cookbook.data.recipes

import com.motorro.cookbook.core.utils.getWeakOrPut
import com.motorro.cookbook.data.recipes.usecase.AddRecipeUsecase
import com.motorro.cookbook.data.recipes.usecase.CategoriesUsecase
import com.motorro.cookbook.data.recipes.usecase.DeleteRecipeUsecase
import com.motorro.cookbook.data.recipes.usecase.RecipeListUsecase
import com.motorro.cookbook.data.recipes.usecase.RecipeUsecase
import com.motorro.cookbook.domain.recipes.RecipeRepository
import com.motorro.cookbook.domain.recipes.data.NewRecipe
import com.motorro.cookbook.domain.recipes.data.RecipeLce
import com.motorro.cookbook.domain.recipes.data.RecipeListLce
import com.motorro.cookbook.model.RecipeCategory
import kotlinx.coroutines.flow.Flow
import java.lang.ref.WeakReference
import javax.inject.Inject
import kotlin.uuid.Uuid

/**
 * Combines use-cases to a single repository
 */
internal class RecipeRepositoryImpl @Inject constructor(
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
        .getWeakOrPut(id) { createRecipeUsecase(id) }
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