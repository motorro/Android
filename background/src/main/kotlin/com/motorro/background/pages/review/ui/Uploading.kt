package com.motorro.background.pages.review.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.motorro.background.R
import com.motorro.background.pages.review.data.ReviewUiState
import com.motorro.composecore.ui.theme.AppDimens
import com.motorro.composecore.ui.theme.AppTheme

@Composable
fun Uploading(
    state: ReviewUiState.Uploading,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().padding(AppDimens.margin_all),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.title_uploading_review),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = AppDimens.spacer)
        )

        LinearProgressIndicator(
            progress = {
                val (current, max) = state.progress
                if (max > 0) current.toFloat() / max else 0f
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

private class UploadingProvider : PreviewParameterProvider<ReviewUiState.Uploading> {
    override val values: Sequence<ReviewUiState.Uploading> = sequenceOf(
        ReviewUiState.Uploading(0 to 100),
        ReviewUiState.Uploading(25 to 100),
        ReviewUiState.Uploading(50 to 100),
        ReviewUiState.Uploading(75 to 100),
        ReviewUiState.Uploading(100 to 100)
    )
}

@Preview
@Composable
private fun UploadingPreview(@PreviewParameter(UploadingProvider::class) state: ReviewUiState.Uploading) {
    AppTheme {
        Uploading(state = state)
    }
}
