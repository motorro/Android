package com.motorro.background.pages.blog.data

import com.motorro.background.pages.blog.repository.data.PostList
import com.motorro.core.lce.LceState

data class BlogUiState(
    val posts: LceState<PostList, Throwable>,
    val refreshActive: Boolean
) {
    companion object Companion {
        val EMPTY = BlogUiState(
            posts = LceState.Loading(),
            refreshActive = false
        )
    }
}