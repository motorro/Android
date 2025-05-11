package com.motorro.cookbook.data

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class UserId(val id: Int) {
    override fun toString(): String = id.toString()
}