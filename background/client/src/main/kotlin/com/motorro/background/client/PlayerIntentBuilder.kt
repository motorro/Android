package com.motorro.background.client

import android.content.Intent

/**
 * Creates service intent
 */
object PlayerIntentBuilder {
    fun getIntent(pkg: String): Intent = Intent(ACTION).apply {
        setPackage(pkg)
    }

    const val ACTION = "com.motorro.background.BIND"
}