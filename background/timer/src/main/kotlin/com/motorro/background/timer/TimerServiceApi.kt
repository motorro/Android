package com.motorro.background.timer

import android.content.Intent

object TimerServiceApi {
    fun getIntent(): Intent = Intent(ACTION).apply {
        setPackage("com.motorro.background")
    }

    private const val ACTION = "com.motorro.background.BIND"
}