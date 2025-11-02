package com.motorro.background.pages.blog.repository.data

import kotlin.time.Instant

/**
 * Post data
 */
data class Post(
    val id: String,
    val authorName: String,
    val title: String,
    val published: Instant
)