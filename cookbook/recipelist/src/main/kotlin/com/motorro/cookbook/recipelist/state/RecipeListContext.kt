package com.motorro.cookbook.recipelist.state

/**
 * Common recipe flow dependencies
 */
internal interface RecipeListContext {
    /**
     * State factory
     */
    val factory: RecipeListStateFactory
}