package com.motorro.di.di

import android.content.Context
import com.motorro.di.R
import com.motorro.di.timer.Timer
import com.motorro.di.timer.TimerImplementation
import dagger.Binds
import dagger.Module
import dagger.Provides

/**
 * Provides dependencies to container
 */
@Module(includes = [ApplicationBindingModule::class])
class ApplicationModule {
    /**
     * App timer title
     */
    @Provides
    fun title(context: Context): String = context.getString(R.string.application_time)

    /**
     * App timer delay
     */
    @Provides
    fun delayMillis(): Long = 100
}

/**
 * Substitutes implementation to all requests for interface
 */
@Module
interface ApplicationBindingModule {
    /**
     * Use [TimerImplementation] if [Timer] is required
     * @param impl Implementation class
     * @return [impl] as [Timer]
     */
    @Binds
    fun timer(impl: TimerImplementation): Timer
}