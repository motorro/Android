package com.motorro.architecture.model.user

import com.motorro.architecture.model.serialization.SerializableUuid
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class UserId(val id: SerializableUuid) {
    override fun toString(): String = "UserID ($id)"
}