package com.motorro.android.player

import android.content.Intent

/**
 * Creates service intent
 */
object PlayerIntentBuilder {
    fun getIntent(pkg: String): Intent = Intent(ACTION).apply {
        setPackage(pkg)
    }

    const val ACTION = "com.motorro.android.player.BIND"
}