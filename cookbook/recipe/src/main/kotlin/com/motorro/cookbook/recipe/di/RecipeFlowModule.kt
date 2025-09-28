package com.motorro.cookbook.recipe.di

import com.motorro.cookbook.appcore.navigation.CommonFlowHost
import com.motorro.cookbook.model.ListRecipe
import com.motorro.cookbook.recipe.api.RecipeApi
import com.motorro.cookbook.recipe.state.RecipeStateFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlin.uuid.Uuid

/**
 * Binds recipe flow components
 */
@Module
@InstallIn(ViewModelComponent::class)
internal class RecipeFlowModule {
    @Provides
    fun api(factory: RecipeStateFactory.Impl.Factory): RecipeApi = object : RecipeApi {
        override fun start(
            recipeId: Uuid,
            flowHost: CommonFlowHost
        ) = factory.create(flowHost).init(recipeId)

        override fun start(
            listRecipe: ListRecipe,
            flowHost: CommonFlowHost
        ) = factory.create(flowHost).init(listRecipe)
    }
}