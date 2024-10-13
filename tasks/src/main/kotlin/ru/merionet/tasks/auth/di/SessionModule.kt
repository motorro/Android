package ru.merionet.tasks.auth.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import ru.merionet.tasks.auth.SessionManager
import ru.merionet.tasks.auth.net.AuthApi
import ru.merionet.tasks.di.App
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