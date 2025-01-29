package com.motorro.coroutines

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(): Unit {
    runBlocking {
        // Child coroutine
        launch {
            repeat(10) { i ->
                log { "I'm blocked ${ i + 1 }" }
                Thread.sleep(300L)
            }
        }
        // Main coroutine
        log { "Hello, world!" }
        Thread.sleep(1000L)
        log { "Goodbye, world!" }
        Thread.sleep(1000L)
        log { "Over and out!" }
    }
    log { "Done!" }
}