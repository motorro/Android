package com.motorro.coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

fun main(): Unit = runBlocking {
    log(thread = true) { "Starting..." }
    withContext(Dispatchers.Default) {
        log(thread = true) { "With Default dispatcher" }
    }
    log(thread = true) { "Stopping..." }
}