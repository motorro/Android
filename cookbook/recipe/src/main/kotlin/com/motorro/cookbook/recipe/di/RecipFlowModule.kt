package com.motorro.cookbook.recipe.di

import com.motorro.cookbook.recipe.state.RecipeStateFactory
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

/**
 * Binds recipe flow components
 */
@Module
@InstallIn(ViewModelComponent::class)
internal abstract class RecipFlowModule {
    @Binds
    abstract fun stateFactory(impl: RecipeStateFactory.Impl): RecipeStateFactory
}