package com.motorro.di.di

import android.app.Application
import android.content.Context
import android.util.Log
import com.motorro.di.R
import com.motorro.di.timer.Timer
import com.motorro.di.timer.TimerImplementation
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Named
import javax.inject.Singleton
import kotlin.random.Random

/**
 * Provides dependencies to container
 */
@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {
    companion object {
        /**
         * Next timer number
         */
        private var nextTimerNumber = 1

        /**
         * Logging tag
         */
        private const val TAG = "ApplicationModule"
    }

    /**
     * App timer delay
     */
    @Provides
    fun delayMillis(): Long = Random.nextLong(50, 500)

    /**
     * Timer provider.
     * Increments creation count each time the new timer is created
     */
    @Provides
    @Named("app")
    @Singleton
    fun timer(context: Context, delay: Long, scope: CoroutineScope): Timer {
        Log.i(TAG, "Creating timer: $nextTimerNumber. Delay would be: $delay")
        return TimerImplementation(
            title = context.getString(R.string.application_time, nextTimerNumber++),
            scope = scope,
            delayMillis = delay
        )
    }
}

@Module
@InstallIn(SingletonComponent::class)
interface ApplicationBindingModule {
    /**
     * Casts default application binding to Context
     */
    @Binds
    fun context(impl: Application): Context
}
