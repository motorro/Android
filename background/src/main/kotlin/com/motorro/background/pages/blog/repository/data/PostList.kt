package com.motorro.background.pages.blog.repository.data

import kotlin.time.Instant

data class PostList(
    val posts: List<Post>,
    val lastUpdated: Instant
)
