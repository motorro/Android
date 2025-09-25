package com.motorro.cookbook.recipe

import androidx.lifecycle.ViewModel
import com.motorro.commonstatemachine.coroutines.FlowStateMachine
import com.motorro.cookbook.recipe.data.RecipeGesture
import com.motorro.cookbook.recipe.data.RecipeViewState
import com.motorro.cookbook.recipe.state.RecipeStateFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlin.uuid.Uuid

@HiltViewModel(assistedFactory = RecipeViewModel.Factory::class)
class RecipeViewModel @AssistedInject internal constructor(factory: RecipeStateFactory, @Assisted recipeId: Uuid) : ViewModel() {

    private val stateMachine = FlowStateMachine<RecipeGesture, RecipeViewState>(RecipeViewState.EMPTY) {
        factory.init(recipeId)
    }

    /**
     * View state
     */
    val viewState: StateFlow<RecipeViewState> = stateMachine.uiState

    /**
     * Gesture processing
     */
    fun process(gesture: RecipeGesture) {
        stateMachine.process(gesture)
    }

    @AssistedFactory
    interface Factory {
        fun create(recipeId: Uuid): RecipeViewModel
    }
}