package com.motorro.cookbook.recipe.state

/**
 * Common recipe flow dependencies
 */
internal interface RecipeContext {
    /**
     * State factory
     */
    val factory: RecipeStateFactory
}