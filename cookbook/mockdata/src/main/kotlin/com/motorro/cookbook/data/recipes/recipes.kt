package com.motorro.cookbook.data.recipes

import com.motorro.cookbook.model.Image
import com.motorro.cookbook.model.Recipe
import com.motorro.cookbook.model.RecipeCategory
import kotlin.time.Instant
import kotlin.uuid.Uuid

/**
 * Sample recipes
 */
val RECIPES = listOf(
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
