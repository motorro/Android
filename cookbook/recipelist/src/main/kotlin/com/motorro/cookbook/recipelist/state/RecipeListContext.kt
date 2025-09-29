package com.motorro.cookbook.recipelist.state

import com.motorro.cookbook.appcore.navigation.CommonFlowHost

/**
 * Common recipe flow dependencies
 */
internal interface RecipeListContext {
    /**
     * State factory
     */
    val factory: RecipeListStateFactory

    /**
     * Flow host
     */
    val flowHost: CommonFlowHost
}