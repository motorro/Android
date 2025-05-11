package com.motorro.cookbook.server

import com.motorro.cookbook.data.Image
import com.motorro.cookbook.data.ListRecipe
import com.motorro.cookbook.data.Recipe
import com.motorro.cookbook.data.RecipeCategory
import kotlin.time.Instant
import kotlin.uuid.Uuid

val recipe: Recipe = Recipe(
    id = Uuid.parse("123e4567-e89b-12d3-a456-426614174000"),
    title = "Test recipe",
    category = RecipeCategory("Test category"),
    image = Image.create("https://example.com/picture.jpg"),
    description = "Test recipe description",
    dateTimeCreated = Instant.parse("2025-05-03T06:57:00Z")
)

val listRecipe: ListRecipe = ListRecipe(
    id = recipe.id,
    title = recipe.title,
    category = recipe.category,
    image = recipe.image,
    dateTimeCreated = recipe.dateTimeCreated
)
