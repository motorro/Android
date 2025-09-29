package com.motorro.cookbook.addrecipe.state

import androidx.lifecycle.SavedStateHandle
import com.motorro.cookbook.domain.recipes.data.NewRecipe
import com.motorro.cookbook.recipe.state.FormState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import javax.inject.Provider

/**
 * Add recipe flow state factory
 */
internal interface AddRecipeStateFactory {
    /**
     * Creates initial state
     */
    fun init(): AddRecipeState = form()

    /**
     * Recipe edit form
     */
    fun form(): AddRecipeState

    /**
     * Saves recipe
     */
    fun saving(toSave: NewRecipe): AddRecipeState

    /**
     * Terminated
     */
    fun terminated(): AddRecipeState

    class Impl @AssistedInject constructor(
        private val createForm: Provider<FormState.Factory>,
        private val createSaving: Provider<SavingState.Factory>,
        @Assisted savedStateHandle: SavedStateHandle
    ) : AddRecipeStateFactory {

        private val context = object : AddRecipeContext {
            override val factory = this@Impl
            override val savedStateHandle = savedStateHandle
        }

        override fun form() = createForm.get()(
            context
        )

        override fun saving(toSave: NewRecipe) = createSaving.get()(
            context,
            toSave
        )

        override fun terminated() = TerminatedState(
            context
        )

        @AssistedFactory
        interface Factory {
            fun create(savedStateHandle: SavedStateHandle): Impl
        }
    }
}