package com.motorro.cookbook.domain.recipes.data

import com.motorro.cookbook.core.error.CoreException
import com.motorro.cookbook.core.lce.LceState
import com.motorro.cookbook.model.Recipe

typealias RecipeLce = LceState<Recipe, CoreException>
