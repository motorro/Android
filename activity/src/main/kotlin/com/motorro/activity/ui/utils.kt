package com.motorro.activity.ui

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.time.Instant

private val DATE_FORMAT = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)

fun Instant.formatLocal(): String = toLocalDateTime(TimeZone.currentSystemDefault())
    .toJavaLocalDateTime()
    .format(DATE_FORMAT)
