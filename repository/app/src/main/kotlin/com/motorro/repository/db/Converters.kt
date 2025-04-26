package com.motorro.repository.db

import androidx.room.TypeConverter
import kotlin.time.Instant
import kotlin.uuid.Uuid

private const val STRING_SEPARATOR = ","

class Converters {
    @TypeConverter
    fun uuidToString(value: Uuid): String = value.toString()

    @TypeConverter
    fun stringToUuid(value: String): Uuid = Uuid.parse(value)

    @TypeConverter
    fun stringListToString(value: List<String>): String = value.joinToString(STRING_SEPARATOR)

    @TypeConverter
    fun stringToStringList(value: String): List<String> = value.split(STRING_SEPARATOR)

    @TypeConverter
    fun instantToLong(value: Instant): Long = value.toEpochMilliseconds()

    @TypeConverter
    fun longToInstant(value: Long): Instant = Instant.fromEpochMilliseconds(value)
}