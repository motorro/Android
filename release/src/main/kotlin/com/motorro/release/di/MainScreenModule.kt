package com.motorro.release.di

import com.motorro.release.api.MainScreenStateApi
import com.motorro.release.api.MainScreenUiApi
import com.motorro.release.state.MainScreenStateFactory
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
}

@Module
@InstallIn(ViewModelComponent::class)
abstract class MainScreenVmBindingModule {
    @Binds
    abstract fun factory(impl: MainScreenStateFactory.Impl): MainScreenStateFactory
}

