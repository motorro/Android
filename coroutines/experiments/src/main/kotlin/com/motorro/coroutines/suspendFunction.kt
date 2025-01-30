package com.motorro.coroutines

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
    withPause()
}

private suspend fun withPause() {
    println("Before first delay")
    delay(1000L)
    println("After first delay")
    delay(1000L)
    println("After second delay")
}