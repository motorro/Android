package com.motorro.coroutines.errors

import com.motorro.coroutines.log
import kotlinx.coroutines.delay


internal suspend fun badFunction() {
    log { "About to fail..." }
    delay(100)
    throw RuntimeException("Bad function!")
}