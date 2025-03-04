package com.motorro.cookbook.data

import com.motorro.cookbook.data.RecipeListItem.CategoryItem
import com.motorro.cookbook.data.RecipeListItem.RecipeItem
import com.motorro.cookbook.mockRecipes
import junit.framework.TestCase.assertEquals
import org.junit.Test

class GetRecipeListKtTest {
    @Test
    fun `transforms recipe list to recipe list items`() {
        val recipes = mockRecipes.shuffled()

        val result = recipes.toRecipeListItems()

        val expected = listOf(
            CategoryItem(RecipeCategory("Category 1")),
            RecipeItem(mockRecipes[0]),
            CategoryItem(RecipeCategory("Category 2")),
            RecipeItem(mockRecipes[1]),
            CategoryItem(RecipeCategory("Category 3")),
            RecipeItem(mockRecipes[2])
        )

        assertEquals(expected, result)
    }
}