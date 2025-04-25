package com.motorro.repository.db

import androidx.room.TypeConverter
import kotlin.uuid.Uuid

class Converters {
    @TypeConverter
    fun uuidToString(value: Uuid): String = value.toString()

    @TypeConverter
    fun stringToUuid(value: String): Uuid = Uuid.parse(value)
}