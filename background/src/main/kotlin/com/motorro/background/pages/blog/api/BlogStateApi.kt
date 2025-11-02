package com.motorro.background.pages.blog.api

import com.motorro.background.api.MainScreenStateApi
import com.motorro.background.data.MainScreenGesture
import com.motorro.background.pages.blog.data.BlogGesture
import com.motorro.background.pages.blog.data.BlogUiState
import com.motorro.background.pages.blog.state.BlogState
import com.motorro.background.pages.blog.state.BlogStateFactory
import com.motorro.core.log.Logging
import javax.inject.Inject

/**
 * Flow API
 */
class BlogStateApi @Inject constructor(private val stateFactory: BlogStateFactory.Impl) : MainScreenStateApi<BlogGesture, BlogUiState>, Logging {
    override val data get() = BlogPageData

    override fun init(data: Any?): BlogState {
        d { "Starting Blog flow..." }
        return stateFactory.work()
    }

    override fun getInitialViewState() = BlogUiState.EMPTY

    override fun mapGesture(parent: MainScreenGesture): BlogGesture? {
        return when {
            parent is MainScreenGesture.PageGesture && data == parent.page -> parent.gesture as BlogGesture
            else -> null
        }
    }
}