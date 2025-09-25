package com.motorro.cookbook.login.di

import com.motorro.cookbook.login.state.LoginStateFactory
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

/**
 * Binds login flow components
 */
@Module
@InstallIn(ViewModelComponent::class)
internal abstract class LoginFlowModule {
    @Binds
    abstract fun stateFactory(impl: LoginStateFactory.Impl): LoginStateFactory
}