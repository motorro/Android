package com.motorro.cookbook.recipe.data

import com.motorro.cookbook.core.lce.LceState
import com.motorro.cookbook.domain.recipes.data.RecipeLce
import kotlin.uuid.Uuid

/**
 * Inter-state data for recipe flow
 */
data class RecipeFlowData(
    val id: Uuid,
    val data: RecipeLce = LceState.Loading(null)
)