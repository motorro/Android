package com.motorro.di.di

import android.content.Context
import android.util.Log
import com.motorro.di.R
import com.motorro.di.timer.Timer
import com.motorro.di.timer.TimerImplementation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.ActivityRetainedLifecycle
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import javax.inject.Named

/**
 * Provides dependencies to main activity
 */
@Module
@InstallIn(ActivityRetainedComponent::class)
class MainActivityModule {
    companion object {
        /**
         * Next timer number
         */
        private var nextTimerNumber = 1

        /**
         * Logging tag
         */
        private const val TAG = "MainActivityModule"
    }

    /**
     * Provides common singleton coroutine scope for view-model dependencies
     * which is cancelled when view-model is destroyed
     */
    @Provides
    @Named("activity")
    @ActivityRetainedScoped
    fun viewModelScope(vmLifecycle: ActivityRetainedLifecycle): CoroutineScope {
        val scope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())
        vmLifecycle.addOnClearedListener { scope.cancel() }
        return scope
    }

    /**
     * Timer provider.
     * Increments creation count each time the new timer is created
     */
    @Provides
    @Named("activity")
    @ActivityRetainedScoped
    fun timer(context: Context, delay: Long, @Named("activity") scope: CoroutineScope): Timer {
        Log.i(TAG, "Creating timer: $nextTimerNumber. Delay would be: $delay")
        return TimerImplementation(
            title = context.getString(R.string.activity_time, nextTimerNumber++),
            scope = scope,
            delayMillis = delay
        )
    }
}
