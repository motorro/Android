package com.motorro.cookbook.recipedemo

import androidx.lifecycle.ViewModel
import com.motorro.commonstatemachine.coroutines.FlowStateMachine
import com.motorro.cookbook.recipe.api.RecipeApi
import com.motorro.cookbook.recipedemo.data.RecipeDemoGesture
import com.motorro.cookbook.recipedemo.data.RecipeDemoViewState
import com.motorro.cookbook.recipedemo.state.StarterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class RecipeDemoViewModel @Inject internal constructor(recipeApi: RecipeApi) : ViewModel() {

    private val stateMachine = FlowStateMachine(RecipeDemoViewState.Starter) {
        StarterState(recipeApi)
    }

    /**
     * View state
     */
    val viewState: StateFlow<RecipeDemoViewState> = stateMachine.uiState

    /**
     * Gesture processing
     */
    fun process(gesture: RecipeDemoGesture) {
        stateMachine.process(gesture)
    }
}