package com.motorro.stateclassic

import android.app.Application
import com.motorro.stateclassic.stat.StatService

class App : Application() {
    /**
     * Stat service
     */
    val statService = StatService.Logger()
}