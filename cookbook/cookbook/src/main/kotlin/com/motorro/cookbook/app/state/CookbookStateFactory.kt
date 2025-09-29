package com.motorro.cookbook.app.state

import javax.inject.Inject
import javax.inject.Provider

/**
 * Main application state factory
 */
interface CookbookStateFactory {
    /**
     * Starts application
     */
    fun init(): CookbookState = recipeListFlow()

    /**
     * Recipe list
     */
    fun recipeListFlow(): CookbookState

    /**
     * End of flow
     */
    fun terminated(): CookbookState

    class Impl @Inject constructor(
        private val createRecipeList: Provider<RecipeListProxy.Factory>
    ) : CookbookStateFactory {

        private val context = object : CookbookContext {
            override val factory: CookbookStateFactory get() = this@Impl
        }

        override fun recipeListFlow() = createRecipeList.get()(
            context
        )

        override fun terminated() = TerminatedState()
    }
}
