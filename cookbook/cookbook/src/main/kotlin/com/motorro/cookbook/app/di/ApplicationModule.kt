package com.motorro.cookbook.app.di

import android.app.Application
import android.content.Context
import com.motorro.cookbook.app.navigation.AppLinks
import com.motorro.cookbook.appcore.navigation.Links
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
    fun links(@Named("Application") context: Context): Links = AppLinks(context)

    @Provides
    fun clock(): Clock = Clock.System
}