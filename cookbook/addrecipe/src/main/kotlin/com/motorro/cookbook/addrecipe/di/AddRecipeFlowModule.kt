package com.motorro.cookbook.addrecipe.di

import androidx.lifecycle.SavedStateHandle
import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.cookbook.addrecipe.api.AddRecipeApi
import com.motorro.cookbook.addrecipe.data.AddRecipeGesture
import com.motorro.cookbook.addrecipe.data.AddRecipeViewState
import com.motorro.cookbook.addrecipe.state.AddRecipeStateFactory
import com.motorro.cookbook.appcore.navigation.CommonFlowHost
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal class AddRecipeFlowModule {
    @Provides
    fun api(savedStateHandle: SavedStateHandle, factoryFactory: AddRecipeStateFactory.Impl.Factory): AddRecipeApi = object : AddRecipeApi {
        override fun start(flowHost: CommonFlowHost): CommonMachineState<AddRecipeGesture, AddRecipeViewState> {
            return factoryFactory.create(savedStateHandle, flowHost).init()
        }
    }
}