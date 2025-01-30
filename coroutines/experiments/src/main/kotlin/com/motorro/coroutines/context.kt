package com.motorro.coroutines

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking(CoroutineName("Main coroutine") + Job()) {
    log { "Hello from: ${coroutineContext[CoroutineName]?.name}" }
}