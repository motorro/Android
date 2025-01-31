package com.motorro.coroutines.errors

import com.motorro.coroutines.log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope

fun main(): Unit = runBlocking {
    // This scope will NOT fail
    supervisorScope {
        launch(Dispatchers.Default) {
            delay(100)
            log { "Throwing first..." }
            throw RuntimeException("First exception")
        }
        launch {
            delay(200)
            log { "Succeeding second..." }
        }
    }
    log { "We're done!" }
}