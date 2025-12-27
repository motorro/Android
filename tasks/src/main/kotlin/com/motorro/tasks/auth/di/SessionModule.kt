package com.motorro.tasks.auth.di

import android.content.Context
import com.motorro.tasks.auth.SessionManager
import com.motorro.tasks.auth.net.AuthApi
import com.motorro.tasks.di.App
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SessionModule {
    @Provides
    @Singleton
    fun sessionManager(
        @ApplicationContext context: Context,
        @App scope: CoroutineScope,
        api: AuthApi
    ): SessionManager = SessionManager.Impl(
        context,
        scope,
        api
    )
}

@Module
@InstallIn(SingletonComponent::class)
interface SessionBindingModule {
    @Binds
    fun authApi(impl: AuthApi.Impl): AuthApi
}