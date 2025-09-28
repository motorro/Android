package com.motorro.cookbook.recipelist.state

import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.cookbook.appcore.navigation.auth.AuthenticationApi
import com.motorro.cookbook.model.ListRecipe
import com.motorro.cookbook.recipelist.data.RecipeListFlowData
import com.motorro.cookbook.recipelist.data.RecipeListGesture
import com.motorro.cookbook.recipelist.data.RecipeListViewState
import javax.inject.Inject
import javax.inject.Provider
import kotlin.uuid.Uuid

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

    /**
     * Switches to the recipe flow when ID is available
     */
    fun recipe(data: RecipeListFlowData, recipeId: Uuid): CommonMachineState<RecipeListGesture, RecipeListViewState>

    /**
     * Switches to the recipe flow when DATA is available
     */
    fun recipe(data: RecipeListFlowData, listRecipe: ListRecipe): CommonMachineState<RecipeListGesture, RecipeListViewState>

    /**
     * Switches to the authentication flow
     */
    fun authenticating(): CommonMachineState<RecipeListGesture, RecipeListViewState>

    class Impl @Inject constructor(
        private val createContent: Provider<ContentState.Factory>,
        private val createLogout: Provider<LoggingOutState.Factory>,
        private val createAddingRecipe: Provider<AddRecipeProxy.Factory>,
        private val createRecipe: Provider<RecipeProxy.Factory>,
        private val createAuthentication: Provider<AuthenticationApi>
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

        override fun recipe(data: RecipeListFlowData, recipeId: Uuid) = createRecipe.get()(
            context,
            data,
            recipeId
        )

        override fun recipe(data: RecipeListFlowData, listRecipe: ListRecipe) = createRecipe.get()(
            context,
            data,
            listRecipe
        )

        override fun authenticating() = createAuthentication.get().createProxy(
            onLoginFactory = { init() },
            onCancelFactory = { terminated() },
            mapGesture = {
                when(it) {
                    is RecipeListGesture.Auth -> it.child
                    else -> null
                }
            },
            mapUiState = { RecipeListViewState.Auth(it) }
        )
    }
}