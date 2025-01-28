package com.motorro.coroutines.network.data

import kotlin.time.Instant

data class Profile(
    val id: Long,
    val name: String,
    val age: Int,
    val registered: Instant
)