package com.motorro.cookbook.recipe.state

import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.cookbook.appcore.navigation.CommonFlowHost
import com.motorro.cookbook.appcore.navigation.auth.AuthenticationApi
import com.motorro.cookbook.core.lce.LceState
import com.motorro.cookbook.model.ListRecipe
import com.motorro.cookbook.model.Recipe
import com.motorro.cookbook.recipe.data.RecipeFlowData
import com.motorro.cookbook.recipe.data.RecipeGesture
import com.motorro.cookbook.recipe.data.RecipeViewState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
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
     * Creates initial state
     */
    fun init(listRecipe: ListRecipe): RecipeState = content(
        with(listRecipe) {
            RecipeFlowData(
                id = id,
                data = LceState.Content(
                    Recipe(
                        id = id,
                        title = title,
                        category = category,
                        image = image,
                        description = "",
                        dateTimeCreated = dateTimeCreated,
                        deleted = deleted
                    )
                )
            )
        }
    )

    /**
     * Recipe loading and display
     */
    fun content(data: RecipeFlowData): RecipeState

    /**
     * Switches to the authentication flow
     * @param recipeId Recipe ID to open after authentication succeeds
     */
    fun authenticating(recipeId: Uuid): CommonMachineState<RecipeGesture, RecipeViewState>

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
    class Impl @AssistedInject constructor(
        private val createContentState: Provider<ContentState.Factory>,
        private val createDeletingState: Provider<DeletingState.Factory>,
        private val createAuthentication: Provider<AuthenticationApi>,
        @Assisted flowHost: CommonFlowHost
    ) : RecipeStateFactory {

        private val context = object : RecipeContext {
            override val factory = this@Impl
            override val flowHost = flowHost
        }

        override fun content(data: RecipeFlowData) = createContentState.get()(
            context,
            data
        )

        override fun authenticating(recipeId: Uuid): CommonMachineState<RecipeGesture, RecipeViewState> = createAuthentication.get().createProxy(
            onLoginFactory = { init(recipeId) },
            onCancelFactory = { terminated() },
            mapGesture = {
                when(it) {
                    is RecipeGesture.Auth -> it.child
                    else -> null
                }
            },
            mapUiState = { RecipeViewState.Auth(it) }
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

        @AssistedFactory
        interface Factory {
            fun create(flowHost: CommonFlowHost): Impl
        }
    }
}
