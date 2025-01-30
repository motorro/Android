package com.motorro.coroutines

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
    val job1: Job = launch {
        log { "Job 1 doing something..." }
        delay(700L)
        log { "Job 1 Done!" }
    }
    val job2: Job = launch {
        log { "Waiting for Job 1..." }
        job1.join()
        log { "Job 2 Done!" }
    }

    log { "Waiting for Job 2..." }
    job2.join()
    log { "We're done!" }
}