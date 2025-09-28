package com.motorro.cookbook.recipelist.state

import com.motorro.commonstatemachine.ProxyMachineState
import com.motorro.cookbook.recipe.api.RecipeApi
import com.motorro.cookbook.recipe.data.RecipeGesture
import com.motorro.cookbook.recipe.data.RecipeViewState
import com.motorro.cookbook.recipelist.data.RecipeListFlowData
import com.motorro.cookbook.recipelist.data.RecipeListGesture
import com.motorro.cookbook.recipelist.data.RecipeListViewState
import javax.inject.Inject
import kotlin.uuid.Uuid

/**
 * Proxy state for recipe flow (for the sake of readability)
 */
private typealias RecipeProxyType = ProxyMachineState<
        RecipeListGesture,
        RecipeListViewState,
        RecipeGesture,
        RecipeViewState>

/**
 * Recipe flow proxy
 */
internal class RecipeProxy(
    private val context: RecipeListContext,
    private val data: RecipeListFlowData,
    private val recipeId: Uuid,
    private val recipeApi: RecipeApi,
) : RecipeProxyType(RecipeApi.DEFAULT_UI_STATE) {

    override fun init() = recipeApi.start(recipeId) {
        setMachineState(context.factory.content(data))
    }

    override fun mapGesture(parent: RecipeListGesture): RecipeGesture? = when (parent) {
        is RecipeListGesture.Back -> RecipeGesture.Back
        is RecipeListGesture.RecipeFlow -> parent.child
        else -> null
    }

    override fun mapUiState(child: RecipeViewState) = RecipeListViewState.Recipe(child)

    /**
     * [RecipeProxy] factory
     */
    class Factory @Inject constructor(private val recipeApi: RecipeApi) {
        operator fun invoke(context: RecipeListContext, data: RecipeListFlowData, recipeId: Uuid) = RecipeProxy(
            context,
            data,
            recipeId,
            recipeApi
        )
    }
}