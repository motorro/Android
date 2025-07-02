package com.motorro.cookbook.domain.recipes.data

import com.motorro.cookbook.core.error.CoreException
import com.motorro.cookbook.core.lce.LceState
import com.motorro.cookbook.model.ListRecipe

typealias RecipeListLce = LceState<List<ListRecipe>, CoreException>
