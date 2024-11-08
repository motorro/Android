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

    /**
     * This method is for use in emulated process environments.  It will
     * never be called on a production Android device, where processes are
     * removed by simply killing them; no user code (including this callback)
     * is executed when doing so.
     */
    override fun onTerminate() {
        super.onTerminate()
    }
}