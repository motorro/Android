package com.motorro.sqlite

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char

val dateFormat = LocalDateTime.Format {
    year(padding = Padding.SPACE)
    char('-')
    monthNumber(padding = Padding.ZERO)
    char('-')
    day(padding = Padding.ZERO)
    char(' ')
    hour(padding = Padding.ZERO)
    char(':')
    minute(padding = Padding.ZERO)
}
