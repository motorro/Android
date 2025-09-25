package com.motorro.cookbook.recipe.state

import com.motorro.cookbook.recipe.data.RecipeFlowData
import javax.inject.Inject
import javax.inject.Provider
import kotlin.uuid.Uuid

/**
 * Recipe flow state factory
 */
internal interface RecipeStateFactory {

    /**
     * Creates initial state
     */
    fun init(recipeId: Uuid): RecipeState = content(RecipeFlowData(recipeId))

    /**
     * Recipe loading and display
     */
    fun content(data: RecipeFlowData): RecipeState

    /**
     * Asks to delete
     */
    fun deleteConfirmation(data: RecipeFlowData): RecipeState

    /**
     * Deleting recipe
     */
    fun deleting(data: RecipeFlowData): RecipeState

    /**
     * Terminated
     */
    fun terminated(): RecipeState

    /**
     * Factory for [RecipeStateFactory]
     */
    class Impl @Inject constructor(
        private val createContentState: Provider<ContentState.Factory>,
        private val createDeletingState: Provider<DeletingState.Factory>,
    ) : RecipeStateFactory {

        private val context = object : RecipeContext {
            override val factory = this@Impl
        }

        override fun content(data: RecipeFlowData) = createContentState.get()(
            context,
            data
        )

        override fun deleteConfirmation(data: RecipeFlowData) = DeleteConfirmationState(
            context,
            data
        )

        override fun deleting(data: RecipeFlowData) = createDeletingState.get()(
            context,
            data
        )

        override fun terminated() = TerminatedState(
            context
        )
    }
}