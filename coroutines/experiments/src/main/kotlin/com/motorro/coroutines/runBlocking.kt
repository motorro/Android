package com.motorro.coroutines

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
    log { "Hello, world!" }
    delay(1000L)
    log { "Goodbye, world!" }
    delay(1000L)
    log { "Over and out!" }
}