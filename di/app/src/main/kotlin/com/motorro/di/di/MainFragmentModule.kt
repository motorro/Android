package com.motorro.di.di

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.motorro.di.R
import com.motorro.di.timer.Timer
import com.motorro.di.timer.TimerImplementation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Named

/**
 * Provides dependencies to main fragment
 */
@Module
@InstallIn(FragmentComponent::class)
class MainFragmentModule {
    companion object {
        /**
         * Next timer number
         */
        private var nextTimerNumber = 1

        /**
         * Logging tag
         */
        private const val TAG = "MainFragmentModule"
    }

    /**
     * Timer provider.
     * Increments creation count each time the new timer is created
     */
    @Provides
    @Named("fragment")
    @FragmentScoped
    fun timer(context: Context, delay: Long, fragment: Fragment): Timer {
        Log.i(TAG, "Creating timer: $nextTimerNumber. Delay would be: $delay")
        return TimerImplementation(
            title = context.getString(R.string.fragment_time, nextTimerNumber++),
            scope = fragment.lifecycleScope,
            delayMillis = delay
        )
    }
}
