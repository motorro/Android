package com.motorro.coroutines

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
    val job1: Job = launch {
        log { "Job 1 doing nothing..." }
        delay(500)
        log { "Job 1 Done!" }
    }
    val job2: Deferred<Int> = async {
        log { "Job 2 doing some job..." }
        delay(500)
        log { "Job 2 Done!" }
        return@async 100500
    }

    log { "Waiting..." }
    job1.join()
    val fromJob2: Int = job2.await()
    log { "We're done! Job 2 returned $fromJob2" }
}