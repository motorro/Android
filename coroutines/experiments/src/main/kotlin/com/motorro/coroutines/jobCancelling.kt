package com.motorro.coroutines

import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
    val job = launch {
        var count = 0
        while (isActive) {
            log { "I'm sleeping ${count++}..." }
            delay(500L)
        }
    }
    log { "Waiting for job..." }
    delay(1300L)
    log { "I'm tired of waiting!" }
    job.cancel()
    log { "Now I can quit!" }
}