package com.motorro.di.di

import android.content.Context
import com.motorro.di.R
import com.motorro.di.timer.Timer
import com.motorro.di.timer.TimerImplementation
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope

/**
 * Provides dependencies to container
 */
@Module
class ApplicationModule(private val context: Context) {
    /**
     * Instructions on how to create timer
     */
    @Provides
    fun timer(scope: CoroutineScope): Timer = TimerImplementation(
        title = context.getString(R.string.application_time),
        scope = scope,
        delayMillis = 100
    )
}