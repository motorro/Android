package com.motorro.cookbook

import com.motorro.cookbook.data.Recipe
import com.motorro.cookbook.data.RecipeCategory

val mockRecipes = listOf(
    Recipe (
        id = 1,
        title = "Recipe 1",
        category = RecipeCategory("Category 1"),
        imageUrl = "https://example.com/image1.jpg",
        steps = emptyList()
    ),
    Recipe (
        id = 2,
        title = "Recipe 2",
        category = RecipeCategory("Category 2"),
        imageUrl = "https://example.com/image2.jpg",
        steps = emptyList()
    ),
    Recipe (
        id = 3,
        title = "Recipe 3",
        category = RecipeCategory("Category 3"),
        imageUrl = "https://example.com/image3.jpg",
        steps = emptyList()
    )
)

val newRecipe = Recipe (
    id = 0,
    title = "Recipe 4",
    category = RecipeCategory("Category 4"),
    imageUrl = "https://example.com/image4.jpg",
    steps = emptyList()
)
