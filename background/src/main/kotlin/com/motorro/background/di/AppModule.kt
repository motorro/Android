package com.motorro.background.di

import com.motorro.background.ServiceMonitor
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlin.time.Clock

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun clock(): Clock = Clock.System
}

@Module
@InstallIn(SingletonComponent::class)
abstract class AppBindingModule {
    @Binds
    @Singleton
    abstract fun serviceMonitor(serviceMonitorImpl: ServiceMonitor.Impl): ServiceMonitor
}