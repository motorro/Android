package com.motorro.cookbook.recipe.state

import com.motorro.cookbook.appcore.navigation.CommonFlowHost

/**
 * Common recipe flow dependencies
 */
internal interface RecipeContext {
    /**
     * State factory
     */
    val factory: RecipeStateFactory

    /**
     * Flow host
     */
    val flowHost: CommonFlowHost
}