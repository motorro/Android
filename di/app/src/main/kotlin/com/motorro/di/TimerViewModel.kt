package com.motorro.di

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.motorro.di.timer.Timer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel

/**
 * Holds timer reference so it is retained through configuration changes
 */
class TimerViewModel(timer: Timer) : ViewModel(), Timer by timer {

    /**
     * To be provided by Dagger
     */
    class Factory(
        private val timer: Timer,
        private val scope: CoroutineScope
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TimerViewModel::class.java)) {
                Log.i(TAG, "Creating view-model... Passed timer: $timer")
                val model = TimerViewModel(timer)
                model.addCloseable(AutoCloseable {
                    scope.cancel()
                })
                return model as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
        }

        companion object {
            private const val TAG = "TimerViewModel.Factory"
        }
    }
}