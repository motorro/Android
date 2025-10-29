package com.motorro.background.pages.blog.state

import com.motorro.background.pages.blog.data.BlogGesture
import com.motorro.background.pages.blog.data.BlogUiState
import com.motorro.commonstatemachine.coroutines.CoroutineState

class BlogState(private val context: BlogContext): CoroutineState<BlogGesture, BlogUiState>() {
    override fun doStart() {
        render()
    }

    private fun render() {
        setUiState(BlogUiState)
    }
}