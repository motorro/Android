package com.motorro.background.pages.blog.di

import com.motorro.background.api.MainScreenStateApi
import com.motorro.background.api.MainScreenUiApi
import com.motorro.background.pages.blog.api.BlogStateApi
import com.motorro.background.pages.blog.api.WorkUiApi
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
    abstract fun blogScreen(impl: WorkUiApi): MainScreenUiApi
}

@Module
@InstallIn(ViewModelComponent::class)
abstract class ApiStateModule {
    @Binds
    @IntoSet
    abstract fun blogState(impl: BlogStateApi): MainScreenStateApi<*, *>
}