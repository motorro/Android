package com.motorro.cookbook.data.recipes

import com.motorro.cookbook.core.error.UnknownException
import com.motorro.cookbook.core.lce.LceState
import com.motorro.cookbook.domain.recipes.RecipeRepository
import com.motorro.cookbook.domain.recipes.data.NewRecipe
import com.motorro.cookbook.domain.recipes.data.RecipeLce
import com.motorro.cookbook.domain.recipes.data.RecipeListLce
import com.motorro.cookbook.domain.session.SessionManager
import com.motorro.cookbook.domain.session.withUserId
import com.motorro.cookbook.model.ListRecipe
import com.motorro.cookbook.model.Recipe
import com.motorro.cookbook.model.RecipeCategory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import java.io.IOException
import kotlin.time.Clock
import kotlin.uuid.Uuid


@OptIn(ExperimentalCoroutinesApi::class)
class MockRepository(
    private val sessionManager: SessionManager,
    recipes: List<Recipe> = RECIPES,
    hasError: Boolean = false
) : RecipeRepository {

    private val _recipes = MutableStateFlow(recipes)

    private val hasError = MutableStateFlow(hasError)

    override val recipes: Flow<RecipeListLce> = sessionManager.withUserId {
        flow {
            emit(LceState.Loading())
            delay(DELAY)
            emitAll(_recipes.map { list ->
                LceState.Content(
                    list.map { r -> ListRecipe(
                        r.id,
                        r.title,
                        r.category,
                        r.image,
                        r.dateTimeCreated
                    ) }
                )
            })
        }
    }

    override fun synchronizeList() {
        hasError.value = false
    }

    override fun getRecipe(id: Uuid): Flow<RecipeLce> = sessionManager.withUserId {
        hasError.flatMapLatest {
            if (it) flow {
                emit(LceState.Loading())
                delay(DELAY)
                emit(LceState.Error(UnknownException(IOException("Network error"))))
            } else flow {
                emit(LceState.Loading())
                delay(DELAY)
                emitAll(_recipes.map { list ->
                    list.find { id == it.id }
                        ?.let { LceState.Content(it) }
                        ?: LceState.Error(UnknownException(NoSuchElementException("Recipe not found")))
                })
            }
        }
    }

    override fun synchronizeRecipe(id: Uuid) {
        hasError.value = false
    }

    override val categories: Flow<List<RecipeCategory>> = flowOf(RECIPES.map { r -> r.category })

    override fun addRecipe(recipe: NewRecipe) {
        _recipes.update { it + Recipe(
            id = Uuid.random(),
            title = recipe.title,
            category = recipe.category,
            image = null,
            description = recipe.description,
            dateTimeCreated = Clock.System.now()
        ) }
    }

    override fun deleteRecipe(id: Uuid) {
        _recipes.update { it.filter { r -> id != r.id } }
    }

    companion object {
        private const val DELAY = 2000L
    }
}


