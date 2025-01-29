package com.motorro.coroutines

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(): Unit {
    runBlocking {
        // Child coroutine
        launch {
            repeat(10) { i ->
                log { "I'm not blocked ${ i + 1 }" }
                delay(300L)
            }
        }
        // Main coroutine
        log { "Hello, world!" }
        delay(1000L)
        log { "Goodbye, world!" }
        delay(1000L)
        log { "Over and out!" }
    }
    log { "Done!" }
}