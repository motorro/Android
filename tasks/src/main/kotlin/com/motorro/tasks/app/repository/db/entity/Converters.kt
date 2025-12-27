package com.motorro.tasks.app.repository.db.entity

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDateTime

/**
 * Converts internal types to those supported by DB
 */
class Converters {
    @TypeConverter
    fun localDateTimeFromString(value: String?): LocalDateTime? = value?.let(LocalDateTime.Formats.ISO::parseOrNull)

    @TypeConverter
    fun localDateTimeToString(value: LocalDateTime?): String? = value?.let(LocalDateTime.Formats.ISO::format)
}