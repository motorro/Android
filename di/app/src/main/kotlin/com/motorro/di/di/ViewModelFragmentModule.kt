package com.motorro.di.di

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.motorro.di.R
import com.motorro.di.TimerViewModel
import com.motorro.di.di.scopes.FragmentScoped
import com.motorro.di.timer.Timer
import com.motorro.di.timer.TimerImplementation
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Named

/**
 * Provides dependencies to view-model fragment
 */
@Module
class ViewModelFragmentModule {
    companion object {
        /**
         * Next timer number
         */
        private var nextTimerNumber = 1

        /**
         * Logging tag
         */
        private const val TAG = "ViewModelFragmentModule"
    }

    /**
     * Provides common singleton coroutine scope for view-model dependencies
     * which is cancelled when view-model is destroyed
     */
    @Provides
    @Named("vm")
    @FragmentScoped
    fun viewModelScope(): CoroutineScope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

    /**
     * Timer provider.
     * Increments creation count each time the new timer is created
     */
    @Provides
    @Named("vm")
    fun timer(context: Context, delay: Long, @Named("vm") scope: CoroutineScope): Timer {
        Log.i(TAG, "Creating timer: $nextTimerNumber. Delay would be: $delay")
        return TimerImplementation(
            title = context.getString(R.string.vm_time, nextTimerNumber++),
            scope = scope,
            delayMillis = delay
        )
    }

    /**
     * Provides view-model factory
     */
    @Provides
    fun viewModelFactory(
        @Named("vm") timer: Timer,
        @Named("vm") scope: CoroutineScope
    ): ViewModelProvider.Factory {
        Log.i(TAG, "Providing view-model factory...")
        return TimerViewModel.Factory(
            timer = timer,
            scope = scope
        )
    }
}
