package com.motorro.cookbook.recipelist.state

import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.cookbook.recipelist.data.RecipeListFlowData
import com.motorro.cookbook.recipelist.data.RecipeListGesture
import com.motorro.cookbook.recipelist.data.RecipeListViewState
import javax.inject.Inject
import javax.inject.Provider

/**
 * Recipe-list flow state factory
 */
internal interface RecipeListStateFactory {

    /**
     * Creates initial state
     */
    fun init(): RecipeListState = content(RecipeListFlowData())

    /**
     * Recipe loading and display
     */
    fun content(data: RecipeListFlowData): RecipeListState

    /**
     * Logging out
     */
    fun loggingOut(): RecipeListState

    /**
     * Terminated
     */
    fun terminated(): RecipeListState

    /**
     * Switches to the add-recipe flow
     */
    fun addingRecipe(data: RecipeListFlowData): CommonMachineState<RecipeListGesture, RecipeListViewState>

    class Impl @Inject constructor(
        private val createContent: Provider<ContentState.Factory>,
        private val createLogout: Provider<LoggingOutState.Factory>,
        private val createAddingRecipe: Provider<AddRecipeProxy.Factory>
    ) : RecipeListStateFactory {

        private val context = object : RecipeListContext {
            override val factory: RecipeListStateFactory get() = this@Impl
        }

        override fun content(data: RecipeListFlowData) = createContent.get()(
            context,
            data
        )

        override fun loggingOut() = createLogout.get()(
            context
        )

        override fun terminated() = TerminatedState(context)

        override fun addingRecipe(data: RecipeListFlowData) = createAddingRecipe.get()(
            context,
            data
        )
    }
}