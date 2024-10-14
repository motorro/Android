package ru.merionet.tasks.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ru.merionet.tasks.app.state.AppStateFactory

@Module
@InstallIn(ViewModelComponent::class)
interface AppFlowModule {
    @Binds
    fun appStateFactory(impl: AppStateFactory.Impl): AppStateFactory
}