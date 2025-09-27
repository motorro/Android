package com.motorro.cookbook.recipelist.state

import com.motorro.commonstatemachine.ProxyMachineState
import com.motorro.cookbook.addrecipe.api.AddRecipeApi
import com.motorro.cookbook.addrecipe.data.AddRecipeGesture
import com.motorro.cookbook.addrecipe.data.AddRecipeViewState
import com.motorro.cookbook.recipelist.data.RecipeListGesture
import com.motorro.cookbook.recipelist.data.RecipeListViewState
import javax.inject.Inject

/**
 * Proxy state for add-recipe flow (for the sake of readability)
 */
private typealias AddRecipeProxyType = ProxyMachineState<
        RecipeListGesture,
        RecipeListViewState,
        AddRecipeGesture,
        AddRecipeViewState>

/**
 * Runs add-recipe flow
 */
internal class AddRecipeProxy(
    private val context: RecipeListContext,
    private val addRecipeApi: AddRecipeApi
) : AddRecipeProxyType(AddRecipeApi.DEFAULT_UI_STATE) {
    override fun init() = addRecipeApi.start {
        setMachineState(context.factory.content())
    }

    override fun mapGesture(parent: RecipeListGesture): AddRecipeGesture? = when (parent) {
        is RecipeListGesture.Back -> AddRecipeGesture.Back
        is RecipeListGesture.AddRecipeFlow -> parent.child
        else -> null
    }

    override fun mapUiState(child: AddRecipeViewState): RecipeListViewState = RecipeListViewState.AddRecipe(
        child
    )

    /**
     * [AddRecipeProxy] factory
     */
    class Factory @Inject constructor(private val addRecipeApi: AddRecipeApi) {
        operator fun invoke(context: RecipeListContext) = AddRecipeProxy(
            context,
            addRecipeApi
        )
    }
}