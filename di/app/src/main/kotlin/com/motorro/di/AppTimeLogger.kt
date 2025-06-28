package com.motorro.di

import android.content.Context
import android.util.Log
import androidx.startup.Initializer
import com.motorro.di.timer.Timer
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.launch
import javax.inject.Named
import kotlin.time.Duration.Companion.seconds

/**
 * Hilt Entry Point demo.
 * Retrieves dependency graph given the application context
 */
@OptIn(FlowPreview::class)
class AppTimeLogger : Initializer<Unit> {
    /**
     * Retrieves dependencies
     */
    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface AppTimeEntryPoint {
        fun scope(): CoroutineScope
        @Named("app") fun timer(): Timer
    }

    override fun create(context: Context): Unit {
        Log.i(TAG, "Installing App Time Logger...")
        val entryPoint = EntryPoints.get(context, AppTimeEntryPoint::class.java)
        entryPoint.scope().launch {
            entryPoint.timer().count.sample(samplePeriod).collect {
                Log.i(TAG, "Time sample: ${it.toDisplayString()}")
            }
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }

    companion object {
        private const val TAG = "AppTimeLogger"
        private val samplePeriod = 1.seconds
    }
}