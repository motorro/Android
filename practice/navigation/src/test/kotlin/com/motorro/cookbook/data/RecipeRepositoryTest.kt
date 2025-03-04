package com.motorro.cookbook.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.motorro.cookbook.mockRecipes
import com.motorro.cookbook.newRecipe
import io.mockk.Ordering
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RecipeRepositoryTest {

    private lateinit var repository: RecipeRepository

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun init() {
        repository = RecipeRepository.Impl(mockRecipes)
    }

    @Test
    fun `returns a list of categories`() {
        val categories = repository.categories.value

        val expected = listOf(
            RecipeCategory("Category 1"),
            RecipeCategory("Category 2"),
            RecipeCategory("Category 3")
        )

        assertEquals(expected, categories)
    }

    @Test
    fun `returns a list of unfiltered items`() {
        val recipes = repository.getRecipes(RecipeFilter()).value

        val expected = listOf(
            mockRecipes[0],
            mockRecipes[1],
            mockRecipes[2]
        )

        assertEquals(expected, recipes)
    }

    @Test
    fun `filters by title`() {
        val recipes = repository.getRecipes(RecipeFilter("2")).value

        val expected = listOf(
            mockRecipes[1],
        )

        assertEquals(expected, recipes)
    }

    @Test
    fun `filters by category`() {
        val recipes = repository.getRecipes(
            RecipeFilter(
                null,
                setOf(
                    RecipeCategory("Category 1"),
                    RecipeCategory("Category 3")
                )
            )
        ).value

        val expected = listOf(
            mockRecipes[0],
            mockRecipes[2]
        )

        assertEquals(expected, recipes)
    }

    @Test
    fun `filters by title and category`() {
        val recipes = repository.getRecipes(
            RecipeFilter(
                "2",
                setOf(RecipeCategory("Category 2"))
            )
        ).value

        val expected = listOf(
            mockRecipes[1]
        )

        assertEquals(expected, recipes)
    }

    @Test
    fun `deletes recipe`() {
        val recipes: Observer<List<Recipe>> = mockk(relaxed = true)
        repository.getRecipes(RecipeFilter()).observeForever(recipes)

        repository.deleteRecipe(2)

        verify(ordering = Ordering.ORDERED) {
            recipes.onChanged(
                listOf(
                    mockRecipes[0],
                    mockRecipes[1],
                    mockRecipes[2]
                )
            )
            recipes.onChanged(
                listOf(
                    mockRecipes[0],
                    mockRecipes[2]
                )
            )
        }
    }

    @Test
    fun `adds recipe`() {
        val recipes: Observer<List<Recipe>> = mockk(relaxed = true)
        repository.getRecipes(RecipeFilter()).observeForever(recipes)

        repository.addRecipe(newRecipe)

        verify(ordering = Ordering.ORDERED) {
            recipes.onChanged(
                listOf(
                    mockRecipes[0],
                    mockRecipes[1],
                    mockRecipes[2]
                )
            )
            recipes.onChanged(
                listOf(
                    mockRecipes[0],
                    mockRecipes[1],
                    mockRecipes[2],
                    newRecipe.copy(id = 4)
                )
            )
        }
    }
}