package com.motorro.cookbook.app.repository

import com.motorro.cookbook.app.data.CookbookError
import com.motorro.cookbook.app.data.NewRecipe
import com.motorro.cookbook.app.data.RecipeLce
import com.motorro.cookbook.app.data.RecipeListLce
import com.motorro.cookbook.app.session.SessionManager
import com.motorro.cookbook.app.session.withUserId
import com.motorro.cookbook.data.Image
import com.motorro.cookbook.data.ListRecipe
import com.motorro.cookbook.data.Recipe
import com.motorro.cookbook.data.RecipeCategory
import com.motorro.core.lce.LceState
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
import kotlin.time.Instant
import kotlin.uuid.Uuid

/**
 * Provides access to the recipes.
 */
interface RecipeRepository {
    /**
     * Returns the list of recipes as a flow with loading state.
     */
    val recipes: Flow<RecipeListLce>

    /**
     * Reloads list data from server
     */
    fun synchronizeList()

    /**
     * Returns the recipe loading state flow with the specified ID.
     * @param id Recipe ID
     */
    fun getRecipe(id: Uuid): Flow<RecipeLce>

    /**
     * Reloads recipe data from server
     * @param id Recipe ID
     */
    fun synchronizeRecipe(id: Uuid)

    /**
     * A list of categories
     */
    val categories: Flow<List<RecipeCategory>>

    /**
     * Adds new recipe
     * @param recipe Recipe to add
     */
    fun addRecipe(recipe: NewRecipe)

    /**
     * Deletes recipe
     * @param id Recipe ID to delete
     */
    fun deleteRecipe(id: Uuid)
}

@OptIn(ExperimentalCoroutinesApi::class)
class MockRepository(private val sessionManager: SessionManager) : RecipeRepository {

    private val _recipes = MutableStateFlow(MockRepository.recipes)

    private val hasError = MutableStateFlow(true)

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
                emit(LceState.Error(CookbookError.Unknown(IOException("Network error"))))
            } else flow {
                emit(LceState.Loading())
                delay(DELAY)
                emitAll(_recipes.map { list ->
                    list.find { id == it.id }
                        ?.let { LceState.Content(it) }
                        ?: LceState.Error(CookbookError.Unknown(NoSuchElementException("Recipe not found")))
                })
            }
        }
    }

    override fun synchronizeRecipe(id: Uuid) {
        hasError.value = false
    }

    override val categories: Flow<List<RecipeCategory>> = flowOf(MockRepository.recipes.map { r -> r.category })

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
        private val recipes = listOf(
            Recipe(
                Uuid.random(),
                "Recipe 1",
                RecipeCategory("Category 1"),
                Image("https://picsum.photos/id/22/200/200"),
                "Description 1",
                Instant.parse("2024-10-01T00:00:00Z"),
            ),
            Recipe(
                Uuid.random(),
                "Recipe 2",
                RecipeCategory("Category 1"),
                Image("https://picsum.photos/id/23/200/200"),
                "Description 2",
                Instant.parse("2024-10-02T00:00:00Z"),
            ),
            Recipe(
                Uuid.random(),
                "Recipe 3",
                RecipeCategory("Category 2"),
                Image("https://picsum.photos/id/24/200/200"),
                "Description 3",
                Instant.parse("2024-10-03T00:00:00Z"),
            )
        )
    }
}

