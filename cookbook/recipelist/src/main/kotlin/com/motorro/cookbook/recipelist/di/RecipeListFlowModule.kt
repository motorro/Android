package com.motorro.cookbook.recipelist.di

import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.cookbook.appcore.navigation.CommonFlowHost
import com.motorro.cookbook.recipelist.api.RecipeListApi
import com.motorro.cookbook.recipelist.data.RecipeListGesture
import com.motorro.cookbook.recipelist.data.RecipeListViewState
import com.motorro.cookbook.recipelist.state.RecipeListStateFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

/**
 * Binds recipe flow components
 */
@Module
@InstallIn(ViewModelComponent::class)
internal class RecipeListFlowModule {
    @Provides
    fun api(factory: RecipeListStateFactory.Impl.Factory): RecipeListApi = object : RecipeListApi {
        override fun start(flowHost: CommonFlowHost): CommonMachineState<RecipeListGesture, RecipeListViewState> {
            return factory.create(flowHost).init()
        }
    }
}