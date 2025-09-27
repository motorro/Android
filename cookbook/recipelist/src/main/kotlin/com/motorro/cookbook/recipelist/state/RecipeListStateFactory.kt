package com.motorro.cookbook.recipelist.state

import javax.inject.Inject
import javax.inject.Provider

/**
 * Recipe-list flow state factory
 */
internal interface RecipeListStateFactory {

    /**
     * Creates initial state
     */
    fun init(): RecipeListState = content()

    /**
     * Recipe loading and display
     */
    fun content(): RecipeListState

    /**
     * Logging out
     */
    fun loggingOut(): RecipeListState

    /**
     * Terminated
     */
    fun terminated(): RecipeListState

    class Impl @Inject constructor(
        private val createContent: Provider<ContentState.Factory>,
        private val createLogout: Provider<LoggingOutState.Factory>
    ) : RecipeListStateFactory {

        private val context = object : RecipeListContext {
            override val factory: RecipeListStateFactory get() = this@Impl
        }

        override fun content() = createContent.get()(
            context
        )

        override fun loggingOut() = createLogout.get()(
            context
        )

        override fun terminated() = TerminatedState(context)
    }
}