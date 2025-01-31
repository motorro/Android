package com.motorro.coroutines.errors

import com.motorro.coroutines.log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
    try {
        // This scope will fail
        coroutineScope {
            launch(Dispatchers.Default) {
                delay(100)
                log { "Throwing first..." }
                throw RuntimeException("First exception")
            }
            launch {
                delay(200)
                log { "Throwing second..." }
                throw RuntimeException("Second exception")
            }
        }
    } catch (e: Throwable) {
        ensureActive()
        log { "Caught exception: $e" }
    }
    log { "We're done!" }
}