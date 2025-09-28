package com.motorro.cookbook.login.di

import com.motorro.cookbook.appcore.navigation.auth.AuthenticationApi
import com.motorro.cookbook.login.api.LoginAuthenticationApi
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Binds login flow components
 */
@Module
@InstallIn(SingletonComponent::class)
internal abstract class LoginFlowModule {
    @Binds
    abstract fun authApi(impl: LoginAuthenticationApi): AuthenticationApi
}