package com.motorro.background.pages.service.di

import com.motorro.background.api.MainScreenStateApi
import com.motorro.background.api.MainScreenUiApi
import com.motorro.background.pages.service.api.ServiceStateApi
import com.motorro.background.pages.service.api.ServiceUiApi
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
    abstract fun serviceScreen(impl: ServiceUiApi): MainScreenUiApi
}

@Module
@InstallIn(ViewModelComponent::class)
abstract class ApiStateModule {
    @Binds
    @IntoSet
    abstract fun serviceState(impl: ServiceStateApi): MainScreenStateApi<*, *>
}