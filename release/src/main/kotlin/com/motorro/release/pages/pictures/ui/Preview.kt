package com.motorro.release.pages.pictures.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.motorro.composecore.ui.theme.AppDimens
import com.motorro.composecore.ui.theme.AppTheme
import com.motorro.release.R
import com.motorro.release.pages.pictures.data.Picture
import com.motorro.release.pages.pictures.data.PicturesGesture
import com.motorro.release.pages.pictures.data.PicturesUiState

@Composable
fun Preview(state: PicturesUiState.Preview, onGesture: (PicturesGesture) -> Unit, modifier: Modifier = Modifier) {
    Gallery(
        pictures = state.pictures,
        onGesture = onGesture,
        modifier = modifier
    )
    Dialog(onDismissRequest = { onGesture(PicturesGesture.Action) }) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppDimens.margin_all),
                verticalArrangement = Arrangement.Center
            ) {
                val selectedPicture = state.pictures[state.selected]
                Icon(
                    imageVector = selectedPicture.vector,
                    contentDescription = stringResource(selectedPicture.title),
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                )
                Text(
                    text = stringResource(selectedPicture.title),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = AppDimens.vertical_margin_small)
                )
                Button(
                    onClick = { onGesture(PicturesGesture.Action) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Close")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPreview() {
    AppTheme {
        Preview(
            state = PicturesUiState.Preview(
                pictures = listOf(
                    Picture(Icons.Default.Album, R.string.p_album),
                    Picture(Icons.Default.Album, R.string.p_album),
                    Picture(Icons.Default.Album, R.string.p_album),
                    Picture(Icons.Default.Album, R.string.p_album)
                ),
                selected = 0
            ),
            onGesture = {}
        )
    }
}