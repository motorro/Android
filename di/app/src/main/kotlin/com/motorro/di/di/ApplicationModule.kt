package com.motorro.di.di

import com.motorro.di.timer.Timer
import com.motorro.di.timer.TimerImplementation
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope

/**
 * Provides dependencies to container
 */
@Module
class ApplicationModule {

    @Provides
    @OptIn(DelicateCoroutinesApi::class)
    fun scope(): CoroutineScope = GlobalScope

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