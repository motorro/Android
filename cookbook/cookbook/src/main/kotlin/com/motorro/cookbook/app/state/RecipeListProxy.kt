package com.motorro.cookbook.app.state

import com.motorro.cookbook.app.data.CookbookGesture
import com.motorro.cookbook.app.data.CookbookViewState
import com.motorro.cookbook.recipelist.api.RecipeListApi
import com.motorro.cookbook.recipelist.data.RecipeListGesture
import com.motorro.cookbook.recipelist.data.RecipeListViewState
import javax.inject.Inject

/**
 * Recipe list
 */
class RecipeListProxy(
    private val cookbookContext: CookbookContext,
    private val api: RecipeListApi
): CookbookProxy<RecipeListGesture, RecipeListViewState>(RecipeListApi.DEFAULT_UI_STATE) {
    override fun init() = api.start {
        setMachineState(cookbookContext.factory.terminated())
    }

    override fun mapGesture(parent: CookbookGesture): RecipeListGesture? = when(parent){
        CookbookGesture.Back -> RecipeListGesture.Back
        is CookbookGesture.RecipeListFlow -> parent.child
    }

    override fun mapUiState(child: RecipeListViewState) = CookbookViewState.RecipeListFlow(child)

    /**
     * [RecipeListProxy] factory
     */
    class Factory @Inject constructor(private val api: RecipeListApi) {
        operator fun invoke(cookbookContext: CookbookContext) = RecipeListProxy(
            cookbookContext,
            api
        )
    }
}