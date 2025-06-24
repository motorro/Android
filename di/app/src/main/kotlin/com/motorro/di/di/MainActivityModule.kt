package com.motorro.di.di

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.motorro.di.R
import com.motorro.di.di.scopes.ActivityScoped
import com.motorro.di.timer.Timer
import com.motorro.di.timer.TimerImplementation
import dagger.Module
import dagger.Provides
import javax.inject.Named

/**
 * Provides dependencies to main activity
 */
@Module
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
     * Timer provider.
     * Increments creation count each time the new timer is created
     */
    @Provides
    @Named("activity")
    @ActivityScoped
    fun timer(context: Context, delay: Long, activity: AppCompatActivity): Timer {
        Log.i(TAG, "Creating timer: $nextTimerNumber. Delay would be: $delay")
        return TimerImplementation(
            title = context.getString(R.string.activity_time, nextTimerNumber++),
            scope = activity.lifecycleScope,
            delayMillis = delay
        )
    }
}
