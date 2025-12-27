package com.motorro.tasks

import android.app.Application
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

/**
 * Our application available to all other components
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize logging
        Napier.base(DebugAntilog())
    }
}