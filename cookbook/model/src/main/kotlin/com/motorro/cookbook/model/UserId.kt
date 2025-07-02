package com.motorro.cookbook.model

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class UserId(val id: Int) {
    override fun toString(): String = id.toString()
}