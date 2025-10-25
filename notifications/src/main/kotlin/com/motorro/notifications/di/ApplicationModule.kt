package com.motorro.notifications.di

import android.app.Application
import androidx.work.WorkManager
import com.motorro.notifications.NotificationActionBuilder
import com.motorro.notifications.NotificationActionBus
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ApplicationModule {
    @Binds
    @Singleton
    abstract fun notificationActionBus(impl: NotificationActionBus.Impl): NotificationActionBus

    @Binds
    abstract fun notificationActionBuilder(impl: NotificationActionBuilder.Impl): NotificationActionBuilder
}

@Module
@InstallIn(SingletonComponent::class)
class ApplicationProviderModule {
    @Provides
    fun workManager(application: Application): WorkManager {
        return WorkManager.getInstance(application.applicationContext)
    }
}