package com.motorro.background.pages.blog.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.zIndex
import com.motorro.background.R
import com.motorro.background.pages.blog.data.BlogGesture
import com.motorro.background.pages.blog.data.BlogUiState
import com.motorro.background.pages.blog.repository.data.Post
import com.motorro.background.pages.blog.repository.data.PostList
import com.motorro.composecore.ui.Loading
import com.motorro.composecore.ui.theme.AppDimens
import com.motorro.composecore.ui.theme.AppTheme
import com.motorro.core.lce.LceState
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

/**
 * Blog screen
 * @param modifier Modifier
 * @param state UI state
 * @param onGesture Gesture callback
 */
@Composable
fun BlogScreen(
    state: BlogUiState,
    onGesture: (BlogGesture) -> Unit,
    modifier: Modifier = Modifier
) {
    val posts = state.posts.data
    if (null == posts) {
        Loading(modifier = modifier.fillMaxSize())
        return
    }
    Box(modifier = modifier.fillMaxSize()) {
        if (state.posts is LceState.Loading) {
            LinearProgressIndicator(Modifier.fillMaxWidth().align(Alignment.TopCenter).zIndex(2F))
        }
        Posts(state, onGesture, Modifier.align(Alignment.TopCenter).zIndex(1F))
    }
}

@Composable
private fun Posts(
    state: BlogUiState,
    onGesture: (BlogGesture) -> Unit,
    modifier: Modifier = Modifier
) {
    val postList = state.posts.data ?: return

    Column(modifier = modifier.fillMaxSize().padding(AppDimens.margin_all)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.last_update, postList.lastUpdated.toLocalDateTime(TimeZone.currentSystemDefault()).time),
                style = MaterialTheme.typography.titleMedium
            )
            Switch(
                checked = state.refreshActive,
                onCheckedChange = { onGesture(BlogGesture.ToggleRefresh) }
            )
        }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(AppDimens.margin_all)) {
            items(postList.posts, key = { it.id }) { post ->
                Post(post = post)
            }
        }
    }
}

@Composable
private fun Post(
    post: Post,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(AppDimens.margin_all), horizontalAlignment = Alignment.Start) {
            Text(
                text = post.title,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = post.authorName,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = post.published.toLocalDateTime(TimeZone.currentSystemDefault()).time.toString(),
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

/**
 * A provider for generating different states of the `WorkScreen` for previews.
 * This allows showcasing the component with various data and enabled states.
 */
private class WorkScreenStateProvider : PreviewParameterProvider<BlogUiState> {
    private val postList = PostList(
        posts = listOf(
            Post("1", "Author 1", "Title 1", Clock.System.now()),
            Post("2", "Author 2", "Title 2", Clock.System.now()),
            Post("3", "Author 3", "Title 3", Clock.System.now())
        ),
        lastUpdated = Clock.System.now()
    )

    override val values: Sequence<BlogUiState> = sequenceOf(
        BlogUiState(LceState.Loading(), false),
        BlogUiState(LceState.Loading(postList), true),
        BlogUiState(LceState.Content(postList), false)
    )
}

@Preview
@Composable
private fun WorkScreenPreviewLoadingNullData(@PreviewParameter(WorkScreenStateProvider::class) state: BlogUiState) {
    AppTheme {
        BlogScreen(
            state = state,
            onGesture = {}
        )
    }
}
