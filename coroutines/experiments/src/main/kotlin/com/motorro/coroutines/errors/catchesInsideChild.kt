package com.motorro.coroutines.errors

import com.motorro.coroutines.log
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
    val job = launch {
        try {
            badFunction()
        } catch (e: Throwable) {
            ensureActive()
            log { "Caught exception: $e" }
        }
    }
    job.join()
    log { "We're done!" }
}
