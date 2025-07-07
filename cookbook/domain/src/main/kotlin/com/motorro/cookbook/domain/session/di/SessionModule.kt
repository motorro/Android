package com.motorro.cookbook.domain.session.di

import com.motorro.cookbook.domain.session.SessionManager
import com.motorro.cookbook.domain.session.SessionManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface SessionModule {
    @Binds
    @Singleton
    fun sessionManager(impl: SessionManagerImpl): SessionManager
}