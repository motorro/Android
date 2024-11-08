package com.motorro.lifecycle

import android.app.Application
import com.motorro.core.log.Logging
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

class App : Application(), Logging {
    override fun onCreate() {
        super.onCreate()
        Napier.base(DebugAntilog())
        i { "Application onCreate" }
    }
}