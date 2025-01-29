package com.motorro.coroutines

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

fun log(time: Boolean = true, thread: Boolean = false, message: () -> String) {
    println(buildString {
        if (time) append("Time: ${Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).time}: ")
        if (thread) append("Thread: ${Thread.currentThread().name}: ")
        append(message())
    })
}