package com.motorro.cookbook.addrecipe

import com.motorro.cookbook.domain.recipes.data.NewRecipe
import com.motorro.cookbook.model.Image
import com.motorro.cookbook.model.RecipeCategory

val NEW_RECIPE = NewRecipe(
    title = "Recipe 1",
    category = RecipeCategory("Category 1"),
    description = "Description 1",
    image = Image("https://picsum.photos/id/22/200/200")
)

