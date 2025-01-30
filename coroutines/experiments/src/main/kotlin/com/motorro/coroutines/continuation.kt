package com.motorro.coroutines

import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

private val dispatcher = Executors.newSingleThreadScheduledExecutor()

fun main(): Unit {
    dispatcher.submit {
        mySuspendFunction()
    }
    dispatcher.awaitTermination(3, TimeUnit.SECONDS)
}

private class Continuation(var resumePoint: Int = 0) {
    fun resume() {
        println("Resuming at $resumePoint")
        mySuspendFunction(this)
    }
}

/**
 * Suspend function like:
 * ```
 * private suspend fun mySuspendFunction() {
 *    println("Before suspend")
 *    delay(1000L)
 *    println("After suspend")
 * }
 */
private fun mySuspendFunction(continuation: Continuation? = null) {
    val cont = continuation ?: Continuation()

    when (cont.resumePoint) {
        0 -> {
            println("Before suspend")
            cont.resumePoint = 1
            dispatcher.schedule(
                { cont.resume() },
                1L,
                TimeUnit.SECONDS
            )
        }
        1 -> {
            println("After suspend")
            dispatcher.shutdown()
        }
    }
}