package com.motorro.di.viewmodel.di

import android.content.Context
import android.util.Log
import com.motorro.di.timer.Timer
import com.motorro.di.timer.TimerImplementation
import com.motorro.di.viewmodel.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.ViewModelLifecycle
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import javax.inject.Named

/**
 * Provides dependencies to view-model fragment
 */
@Module
@InstallIn(ViewModelComponent::class)
internal class ViewModelFragmentModule {
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
    @ViewModelScoped
    fun viewModelScope(vmLifecycle: ViewModelLifecycle): CoroutineScope {
        val scope = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())
        vmLifecycle.addOnClearedListener { scope.cancel() }
        return scope
    }

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
}
