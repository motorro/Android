package com.motorro.coroutines

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
    val job: Job = launch {
        log { "Doing something..." }
        delay(700L)
        log { "Done!" }
    }
    log { "Waiting for child..." }
    job.join()
    log { "We're done!" }
}