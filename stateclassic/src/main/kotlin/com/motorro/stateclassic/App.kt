package com.motorro.stateclassic

import android.app.Application
import com.motorro.stateclassic.prefs.KeyValueStorage
import com.motorro.stateclassic.prefs.SharedPreferencesStorage
import com.motorro.stateclassic.stat.StatService

class App : Application() {
    /**
     * Stat service
     */
    val statService = StatService.Logger()

    /**
     * Preferences storage
     */
    val preferences: KeyValueStorage by lazy {
        SharedPreferencesStorage(this, "prefs")
    }
}