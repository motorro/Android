package com.motorro.background.client

import android.app.Application
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize logging
        Napier.base(DebugAntilog())
    }
}