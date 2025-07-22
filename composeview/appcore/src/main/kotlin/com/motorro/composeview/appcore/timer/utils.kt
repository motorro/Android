package com.motorro.composeview.appcore.timer

import java.util.Locale
import kotlin.time.Duration

/**
 * Formats [Duration]
 */
fun Duration.toDisplayString(): String {
    val components = this.toComponents { minutes, seconds, nanos -> arrayOf(minutes.toInt(), seconds, nanos / 10_000_000) }

    return String.format(
        Locale.getDefault(),
        "%02d:%02d.%02d",
        *components
    )
}
