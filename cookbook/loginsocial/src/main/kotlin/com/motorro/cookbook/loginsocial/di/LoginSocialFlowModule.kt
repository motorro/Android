package com.motorro.cookbook.loginsocial.di

import com.motorro.cookbook.appcore.navigation.auth.AuthenticationApi
import com.motorro.cookbook.loginsocial.api.LoginSocialAuthenticationApi
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Binds login flow components
 */
@Module
@InstallIn(SingletonComponent::class)
internal abstract class LoginSocialFlowModule {
    @Binds
    abstract fun authApi(impl: LoginSocialAuthenticationApi): AuthenticationApi
}