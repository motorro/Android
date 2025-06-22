package com.motorro.di.di

import android.content.Context
import com.motorro.di.R
import dagger.Module
import dagger.Provides

/**
 * Provides dependencies to container
 */
@Module
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