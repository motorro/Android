package com.motorro.background.pages.blog.state

import com.motorro.background.pages.blog.data.BlogGesture
import com.motorro.background.pages.blog.data.BlogUiState
import com.motorro.background.pages.blog.repository.BlogRepository
import com.motorro.background.pages.blog.repository.data.PostList
import com.motorro.commonstatemachine.coroutines.CoroutineState
import com.motorro.core.lce.LceState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class BlogState(private val context: BlogContext, private val blogRepository: BlogRepository): CoroutineState<BlogGesture, BlogUiState>(), BlogContext by context {

    private val postList: StateFlow<LceState<PostList, Throwable>> = blogRepository.latestPosts.stateIn(
        scope = stateScope,
        started = SharingStarted.Lazily,
        initialValue = LceState.Loading()
    )

    override fun doStart() {
        subscribeRepo()
    }

    private fun subscribeRepo() = stateScope.launch {
        postList.collect {
            render()
        }
    }

    override fun doProcess(gesture: BlogGesture) {
        when (gesture) {
            BlogGesture.Refresh -> stateScope.launch {
                blogRepository.refresh()
            }
        }
    }

    private fun render() {
        setUiState(BlogUiState(postList.value))
    }

    class Factory @Inject constructor(private val blogRepository: BlogRepository) {
        operator fun invoke(context: BlogContext) = BlogState(context, blogRepository)
    }
}