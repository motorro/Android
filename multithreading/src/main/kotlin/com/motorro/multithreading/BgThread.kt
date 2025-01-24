package com.motorro.multithreading

import android.os.Handler
import android.os.Looper

class BgThread : Thread() {
    private lateinit var handler: Handler

    init {
        this.name = "BgThread"
    }

    override fun run() {
        super.run()
        log { "Running thread..." }
        Looper.prepare()
        handler = Handler(requireNotNull(Looper.myLooper()) { "Should have a looper" })
        Looper.loop()
        log { "Thread complete" }
    }

    fun post(runnable: Runnable) {
        handler.post(runnable)
    }

    fun terminate() {
        post {
            Looper.myLooper()?.quit()
        }
    }
}