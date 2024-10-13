package ru.merionet.tasks.login.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ru.merionet.tasks.login.state.LoginStateFactory

@Module
@InstallIn(ViewModelComponent::class)
interface LoginModule {
    @Binds
    fun loginStateFactory(impl: LoginStateFactory.Impl): LoginStateFactory
}