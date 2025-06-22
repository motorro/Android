package com.motorro.di.di

import com.motorro.di.timer.Timer
import com.motorro.di.timer.TimerImplementation
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope

/**
 * Provides dependencies to container
 */
@Module
class ApplicationModule {
    /**
     * Instructions on how to create timer
     */
    @Provides
    fun timer(scope: CoroutineScope): Timer = TimerImplementation(
        title = "Application time",
        scope = scope,
        delayMillis = 100
    )
}