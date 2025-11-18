package com.motorro.cookbook.app.di

import android.app.Application
import android.content.Context
import androidx.work.WorkManager
import com.motorro.cookbook.domain.session.UserHandler
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import javax.inject.Named
import kotlin.time.Clock

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {
    @Provides
    @Named("Application")
    fun applicationScope(): CoroutineScope = MainScope()

    @Provides
    @Named("Application")
    fun context(app: Application): Context = app

    @Provides
    fun clock(): Clock = Clock.System

    @Provides
    fun workManager(application: Application): WorkManager = WorkManager.getInstance(application)
}

@Module
@InstallIn(SingletonComponent::class)
abstract class ApplicationBindingModule {
    @Binds
    @IntoSet
    abstract fun loggingUserHandler(impl: LoggingUserHandler): UserHandler
}