package com.motorro.background.di

import android.app.Application
import android.content.Context
import com.motorro.background.api.MainScreenStateApi
import com.motorro.background.api.MainScreenUiApi
import com.motorro.background.state.MainScreenStateFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Module
@InstallIn(ActivityComponent::class)
class MainScreenUiModule {
    @Provides
    fun pages(set: @JvmSuppressWildcards Set<MainScreenUiApi>): ImmutableList<MainScreenUiApi> {
        return set.sortedBy { it.data.index }.toImmutableList()
    }
}

@Module
@InstallIn(ViewModelComponent::class)
class MainScreenVmModule {
    @Provides
    fun pages(set: @JvmSuppressWildcards Set<MainScreenStateApi<*, *>>): ImmutableList<MainScreenStateApi<*, *>> {
        return set.sortedBy { it.data.index }.toImmutableList()
    }

    @Provides
    fun context(application: Application): Context = application.applicationContext
}

@Module
@InstallIn(ViewModelComponent::class)
abstract class MainScreenVmBindingModule {
    @Binds
    abstract fun factory(impl: MainScreenStateFactory.Impl): MainScreenStateFactory
}

