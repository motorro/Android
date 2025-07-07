package com.motorro.cookbook.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * The application. Uses Hilt to provide dependencies
 */
@HiltAndroidApp
class App : Application()