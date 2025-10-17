package com.motorro.notifications.pages.reply.ui

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
import com.motorro.notifications.pages.reply.data.ReplyViewState

@Composable
fun ReplyScreen(state: ReplyViewState, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(AppDimens.margin_all),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (state.reply != null) {
            Card {
                Text(
                    text = state.reply,
                    modifier = Modifier.padding(AppDimens.margin_all),
                    style = MaterialTheme.typography.bodyLarge
                )
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
                        text = stringResource(R.string.reply_screen_no_reply),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

class ReplyViewStateProvider : PreviewParameterProvider<ReplyViewState> {
    override val values: Sequence<ReplyViewState> = sequenceOf(
        ReplyViewState(reply = "This is a sample reply."),
        ReplyViewState(reply = null)
    )
}

@Preview
@Composable
private fun ReplyScreenWithTextPreview(@PreviewParameter(ReplyViewStateProvider::class) state: ReplyViewState) {
    AppTheme {
        ReplyScreen(state = state)
    }
}
