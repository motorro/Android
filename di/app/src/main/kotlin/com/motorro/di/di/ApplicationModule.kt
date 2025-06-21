package com.motorro.di.di

import com.motorro.di.timer.Timer
import com.motorro.di.timer.TimerImplementation
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope

/**
 * Provides dependencies to container
 */
@Module
class ApplicationModule {

    /**
     * Instructions on how to create timer
     */
    @Provides
    @OptIn(DelicateCoroutinesApi::class)
    fun timer(): Timer = TimerImplementation(
        title = "Application time",
        scope = GlobalScope,
        delayMillis = 100
    )

}