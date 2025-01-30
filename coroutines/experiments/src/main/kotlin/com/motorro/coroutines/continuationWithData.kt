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

private class ContinuationWithData(var resumePoint: Int = 0, var someData: Int = 0) {
    fun resume() {
        println("Resuming at $resumePoint")
        mySuspendFunction(this)
    }
}

/**
 * Suspend function like:
 * ```
 * private suspend fun mySuspendFunction() {
 *    var someData = 0
 *    println("Before suspend: Data: $someData")
 *    someData = 100500
 *    delay(1000L)
 *    println("After suspend: Data: $someData")
 * }
 */
private fun mySuspendFunction(continuation: ContinuationWithData? = null) {
    val cont = continuation ?: ContinuationWithData()

    var someData = cont.someData
    when (cont.resumePoint) {
        0 -> {
            println("Before suspend: Data: $someData")
            someData = 100500
            cont.resumePoint = 1
            cont.someData = someData
            dispatcher.schedule(
                { cont.resume() },
                1L,
                TimeUnit.SECONDS
            )
        }
        1 -> {
            println("After suspend. Data: $someData")
            dispatcher.shutdown()
        }
    }
}