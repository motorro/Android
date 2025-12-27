package com.motorro.tasks.di

import com.motorro.tasks.app.state.AppStateFactory
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface AppFlowModule {
    @Binds
    fun appStateFactory(impl: AppStateFactory.Impl): AppStateFactory
}