package com.motorro.background.pages.review.di

import com.motorro.background.api.MainScreenStateApi
import com.motorro.background.api.MainScreenUiApi
import com.motorro.background.pages.review.api.ReviewStateApi
import com.motorro.background.pages.review.api.ReviewUiApi
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
    abstract fun reviewScreen(impl: ReviewUiApi): MainScreenUiApi
}

@Module
@InstallIn(ViewModelComponent::class)
abstract class ApiStateModule {
    @Binds
    @IntoSet
    abstract fun reviewState(impl: ReviewStateApi): MainScreenStateApi<*, *>
}