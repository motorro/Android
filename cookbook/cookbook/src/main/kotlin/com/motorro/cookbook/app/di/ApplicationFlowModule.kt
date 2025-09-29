package com.motorro.cookbook.app.di

import com.motorro.cookbook.app.state.CookbookStateFactory
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ApplicationFlowModule {
    @Binds
    abstract fun factory(impl: CookbookStateFactory.Impl): CookbookStateFactory
}