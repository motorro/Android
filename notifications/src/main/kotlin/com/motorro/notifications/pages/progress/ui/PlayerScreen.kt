package com.motorro.notifications.pages.progress.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.unit.dp
import com.motorro.composecore.ui.theme.AppDimens
import com.motorro.composecore.ui.theme.AppTheme
import com.motorro.notifications.R
import com.motorro.notifications.pages.progress.data.ProgressGesture
import com.motorro.notifications.pages.progress.data.ProgressViewState
import com.motorro.notifications.pages.progress.data.ProgressViewState.Player

@Composable
fun PlayerScreen(state: Player, modifier: Modifier = Modifier, onGesture: (ProgressGesture) -> Unit) {
    Column(
        modifier = modifier.padding(AppDimens.margin_all).fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(AppDimens.spacer_small, Alignment.CenterVertically),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = stringResource(R.string.lbl_download_progress),
            style = MaterialTheme.typography.headlineSmall
        )

        val progress = when (state) {
            is Player.InProgress -> state.progress.toFloat() / 100
            Player.Idle -> 0f
        }
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth()
        )

        if (state is Player.InProgress) {
            Text(
                modifier = Modifier.height(AppDimens.vertical_margin),
                text = state.phaseName,
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            Spacer(modifier = Modifier.height(AppDimens.vertical_margin))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AppDimens.horizontal_margin_small, Alignment.End),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val btnWidth = 120.dp
            Button(
                modifier = Modifier.width(btnWidth),
                onClick = { onGesture(ProgressGesture.Stop) },
                enabled = state is Player.InProgress
            ) {
                Icon(imageVector = Icons.Default.Stop, contentDescription = stringResource(R.string.btn_stop))
                Text(text = stringResource(R.string.btn_stop))
            }

            Button(
                modifier = Modifier.width(btnWidth),
                onClick = { onGesture(ProgressGesture.Play) },
                enabled = state is Player.Idle
            ) {
                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = stringResource(R.string.btn_play))
                Text(text = stringResource(R.string.btn_play))
            }
        }
    }
}

class ProgressViewStateProvider : PreviewParameterProvider<ProgressViewState> {
    override val values: Sequence<Player> = sequenceOf(
        Player.Idle,
        Player.InProgress(progress = 30, phaseName = "Downloading..."),
    )
}

@Preview
@Composable
private fun ProgressScreenPreview(@PreviewParameter(ProgressViewStateProvider::class) state: Player) {
    AppTheme {
        PlayerScreen(state = state, onGesture = {})
    }
}