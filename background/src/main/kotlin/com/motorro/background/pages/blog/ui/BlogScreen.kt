package com.motorro.background.pages.blog.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.motorro.background.R
import com.motorro.background.pages.blog.data.BlogGesture
import com.motorro.background.pages.blog.data.BlogUiState
import com.motorro.composecore.ui.theme.AppDimens

/**
 * Work screen
 * @param modifier Modifier
 * @param state UI state
 * @param onGesture Gesture callback
 */
@Composable
fun ServiceScreen(
    state: BlogUiState,
    onGesture: (BlogGesture) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().padding(AppDimens.margin_all)
    ) {
        Text(
            text = stringResource(R.string.page_blog),
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}
