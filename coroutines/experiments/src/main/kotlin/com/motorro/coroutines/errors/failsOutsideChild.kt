package com.motorro.coroutines.errors

import com.motorro.coroutines.log
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
    try {
        val job = launch {
            badFunction()
        }
        job.join()
    } catch (e: Throwable) {
        ensureActive()
        log { "Caught exception: $e" }
    }
    log { "We're done!" }
}
