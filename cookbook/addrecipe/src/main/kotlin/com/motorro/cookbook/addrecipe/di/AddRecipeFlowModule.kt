package com.motorro.cookbook.addrecipe.di

import androidx.lifecycle.SavedStateHandle
import com.motorro.cookbook.addrecipe.state.AddRecipeStateFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal class AddRecipeFlowModule {
    @Provides
    fun stateFactory(savedStateHandle: SavedStateHandle, factoryFactory: AddRecipeStateFactory.Impl.Factory): AddRecipeStateFactory {
        return factoryFactory.create(savedStateHandle)
    }
}