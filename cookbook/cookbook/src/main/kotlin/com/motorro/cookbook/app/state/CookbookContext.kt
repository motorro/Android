package com.motorro.cookbook.app.state

/**
 * Application context
 */
interface CookbookContext {
    /**
     * Application state factory
     */
    val factory: CookbookStateFactory
}