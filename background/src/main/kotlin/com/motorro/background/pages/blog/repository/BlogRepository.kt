package com.motorro.background.pages.blog.repository

import com.motorro.background.Constants
import com.motorro.background.pages.blog.repository.data.PostList
import com.motorro.core.lce.LceState
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Blog repository
 */
interface BlogRepository {
    /**
     * Latest posts
     */
    val latestPosts: Flow<LceState<PostList, Throwable>>

    /**
     * Refreshes the latest posts
     */
    suspend fun refresh()

    class Impl @Inject constructor(private val blogApi: BlogApi) : BlogRepository {

        private val latestPostsState = MutableStateFlow<LceState<PostList, Throwable>>(LceState.Loading())

        override val latestPosts: Flow<LceState<PostList, Throwable>> = flow {
            coroutineScope {
                launch {
                    refresh()
                }
                emitAll(latestPostsState.filterNotNull())
            }
        }

        override suspend fun refresh() {
            latestPostsState.value = LceState.Loading(latestPostsState.value.data)
            latestPostsState.value = LceState.Content(blogApi.getLatestPosts(Constants.LATEST_POSTS_TO_STORE))
        }
    }
}