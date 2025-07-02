package com.motorro.cookbook.data.recipes.db.entity

import androidx.room.TypeConverter
import com.motorro.cookbook.model.UserId
import kotlin.time.Instant
import kotlin.uuid.Uuid

class Converters {
    @TypeConverter
    fun fromUserId(value: UserId): Int = value.id

    @TypeConverter
    fun toUserId(value: Int): UserId = UserId(value)

    @TypeConverter
    fun fromUuid(value: Uuid): String = value.toString()

    @TypeConverter
    fun toUuid(value: String): Uuid = Uuid.parse(value)

    @TypeConverter
    fun fromInstant(value: Instant): String = value.toString()

    @TypeConverter
    fun toInstant(value: String): Instant = Instant.parse(value)
}