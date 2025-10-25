package com.motorro.notifications.pages.push.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.motorro.composecore.ui.theme.AppDimens
import com.motorro.composecore.ui.theme.AppTheme
import com.motorro.notifications.R
import com.motorro.notifications.pages.push.data.FromPush
import com.motorro.notifications.pages.push.data.PushViewState

@Composable
fun PushScreen(state: PushViewState, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(AppDimens.margin_all),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (state.fromPush != null) {
            Card {
                Column(
                    modifier = Modifier.padding(AppDimens.margin_all),
                    verticalArrangement = Arrangement.spacedBy(AppDimens.spacer_small)
                ) {
                    Text(
                        text = stringResource(R.string.push_screen_title),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = state.fromPush.title ?: "",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = stringResource(R.string.push_screen_message),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = state.fromPush.message ?: "",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = stringResource(R.string.push_screen_data),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = state.fromPush.data ?: stringResource(R.string.push_screen_no_data_placeholder),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        } else {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
            ) {
                Column(
                    modifier = Modifier.padding(AppDimens.margin_all),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(AppDimens.spacer_small)
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null
                    )
                    Text(
                        text = stringResource(R.string.push_screen_no_data),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

class PushViewStateProvider : PreviewParameterProvider<PushViewState> {
    override val values: Sequence<PushViewState> = sequenceOf(
        PushViewState(FromPush(
            title = "Sample Title",
            message = "This is a sample message.",
            data = "This is a sample data."
        )),
        PushViewState(FromPush(
            title = "Sample Title",
            message = "This is a sample message.",
            data = null
        )),
        PushViewState()
    )
}

@Preview
@Composable
private fun ReplyScreenWithTextPreview(@PreviewParameter(PushViewStateProvider::class) state: PushViewState) {
    AppTheme {
        PushScreen(state = state)
    }
}
