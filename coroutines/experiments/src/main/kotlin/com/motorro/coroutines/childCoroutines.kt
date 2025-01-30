package com.motorro.coroutines

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
    launch {
        delay(1000L)
        log { "I'm child one" }
    }
    launch {
        delay(500L)
        log { "I'm child two" }
    }
    log { "I'm parent" }
}