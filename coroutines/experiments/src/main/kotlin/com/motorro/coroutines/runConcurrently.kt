package com.motorro.coroutines

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(): Unit {
    runBlocking {
        // Child coroutine
        launch {
            repeat(10) { i ->
                log(thread = true) { "I'm not blocked ${ i + 1 }" }
                delay(300L)
            }
        }
        // Main coroutine
        log(thread = true) { "Hello, world!" }
        delay(1000L)
        log(thread = true) { "Goodbye, world!" }
        delay(1000L)
        log(thread = true) { "Over and out!" }
    }
    log(thread = true) { "Done!" }
}