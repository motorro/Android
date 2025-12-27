package com.motorro.tasks.login.di

import com.motorro.tasks.login.state.LoginStateFactory
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface LoginModule {
    @Binds
    fun loginStateFactory(impl: LoginStateFactory.Impl): LoginStateFactory
}