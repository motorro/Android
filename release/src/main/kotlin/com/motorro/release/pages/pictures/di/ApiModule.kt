package com.motorro.release.pages.pictures.di

import com.motorro.release.api.MainScreenStateApi
import com.motorro.release.api.MainScreenUiApi
import com.motorro.release.pages.pictures.api.PicturesStateApi
import com.motorro.release.pages.pictures.api.PicturesUiApi
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
    abstract fun picturesScreen(impl: PicturesUiApi): MainScreenUiApi
}

@Module
@InstallIn(ViewModelComponent::class)
abstract class ApiStateModule {
    @Binds
    @IntoSet
    abstract fun picturesState(impl: PicturesStateApi): MainScreenStateApi<*, *>
}