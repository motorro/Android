package com.motorro.sqlite.data

import android.net.Uri
import androidx.room.TypeConverter
import kotlinx.datetime.LocalDateTime

class Converters {
    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime): String = value.toString()

    @TypeConverter
    fun toLocalDateTime(value: String): LocalDateTime = LocalDateTime.parse(value)

    @TypeConverter
    fun fromUri(value: Uri): String = value.toString()

    @TypeConverter
    fun toUri(value: String): Uri = Uri.parse(value)
}