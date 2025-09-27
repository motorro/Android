package com.motorro.cookbook.recipelist.di

import com.motorro.cookbook.recipelist.state.RecipeListStateFactory
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

/**
 * Binds recipe flow components
 */
@Module
@InstallIn(ViewModelComponent::class)
internal abstract class RecipeListFlowModule {
    @Binds
    abstract fun stateFactory(impl: RecipeListStateFactory.Impl): RecipeListStateFactory
}