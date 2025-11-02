package com.motorro.background.pages.blog.state

import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkQuery
import com.motorro.background.pages.blog.BlogRefreshWorker
import com.motorro.background.pages.blog.data.BlogGesture
import com.motorro.background.pages.blog.data.BlogUiState
import com.motorro.background.pages.blog.repository.BlogRepository
import com.motorro.background.pages.blog.repository.data.PostList
import com.motorro.commonstatemachine.coroutines.CoroutineState
import com.motorro.core.lce.LceState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class BlogState(private val context: BlogContext, private val blogRepository: BlogRepository): CoroutineState<BlogGesture, BlogUiState>(), BlogContext by context {

    private val postList: StateFlow<LceState<PostList, Throwable>> = blogRepository.latestPosts.stateIn(
        scope = stateScope,
        started = SharingStarted.Lazily,
        initialValue = LceState.Loading()
    )

    val workManager: WorkManager get() = WorkManager.getInstance(appContext)

    /**
     * Job status
     */
    private val refreshRegistered: StateFlow<Boolean> = workManager
        .getWorkInfosFlow(WorkQuery.fromTags(BlogRefreshWorker.TAG))
        .map { it.any { workInfo -> workInfo.state.isActive } }
        .stateIn(stateScope, SharingStarted.Lazily, false)


    override fun doStart() {
        subscribeRepo()
        subscribeWorker()
    }

    private fun subscribeRepo() = stateScope.launch {
        postList.collect {
            render()
        }
    }

    private fun subscribeWorker() = stateScope.launch {
        refreshRegistered.collect {
            render()
        }
    }

    override fun doProcess(gesture: BlogGesture) {
        when (gesture) {
            BlogGesture.ToggleRefresh -> toggleRefreshWork()
        }
    }

    private fun render() {
        setUiState(
            BlogUiState(
                posts = postList.value,
                refreshActive = refreshRegistered.value
            )
        )
    }

    private fun toggleRefreshWork() {
        if (refreshRegistered.value) {
            workManager.cancelAllWorkByTag(BlogRefreshWorker.TAG)
        } else {
            workManager.enqueue(BlogRefreshWorker.createJob())
        }
    }

    companion object {
        /**
         * Checks if the work is active
         */
        val WorkInfo.State.isActive: Boolean
            get() = this == WorkInfo.State.ENQUEUED
                || this == WorkInfo.State.RUNNING
                || this == WorkInfo.State.BLOCKED
    }

    class Factory @Inject constructor(private val blogRepository: BlogRepository) {
        operator fun invoke(context: BlogContext) = BlogState(context, blogRepository)
    }
}