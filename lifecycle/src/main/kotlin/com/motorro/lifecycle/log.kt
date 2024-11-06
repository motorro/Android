package com.motorro.lifecycle

import androidx.lifecycle.LifecycleOwner
import com.motorro.core.log.Logging

fun Logging.logCurrentState(owner: LifecycleOwner, point: String? = null) {
    i { "Current state${ point?.let { " at $it" }}: ${owner.lifecycle.currentState}" }
}