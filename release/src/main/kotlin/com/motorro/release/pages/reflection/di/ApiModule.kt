package com.motorro.release.pages.reflection.di

import com.motorro.release.api.MainScreenStateApi
import com.motorro.release.api.MainScreenUiApi
import com.motorro.release.pages.reflection.api.ReflectionStateApi
import com.motorro.release.pages.reflection.api.ReflectionUiApi
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityComponent::class)
abstract class ApiUiModule {
    @Binds
    @IntoSet
    abstract fun reflectionScreen(impl: ReflectionUiApi): MainScreenUiApi
}

@Module
@InstallIn(ViewModelComponent::class)
abstract class ApiStateModule {
    @Binds
    @IntoSet
    abstract fun reflectionState(impl: ReflectionStateApi): MainScreenStateApi<*, *>
}