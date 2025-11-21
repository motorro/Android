package com.motorro.release.pages.pictures.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Album
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.motorro.composecore.ui.theme.AppDimens
import com.motorro.composecore.ui.theme.AppTheme
import com.motorro.release.R
import com.motorro.release.pages.pictures.data.Picture
import com.motorro.release.pages.pictures.data.PicturesGesture

@Composable
fun Gallery(
    pictures: List<Picture>,
    onGesture: (PicturesGesture) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = AppDimens.list_image_size),
        modifier = modifier.fillMaxSize()
    ) {
        itemsIndexed(pictures) { index, picture ->
            Card(
                modifier = Modifier
                    .padding(AppDimens.margin_all_small)
                    .clickable { onGesture(PicturesGesture.PictureClicked(index)) }
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = picture.vector,
                        contentDescription = stringResource(picture.title),
                        modifier = Modifier
                            .size(AppDimens.list_image_size)
                            .aspectRatio(1f)
                    )
                    Text(
                        text = stringResource(picture.title),
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.MiddleEllipsis,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(AppDimens.margin_all_small)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GalleryPreview() {
    AppTheme {
        Gallery(
            pictures = listOf(
                Picture(Icons.Default.Album, R.string.p_album),
                Picture(Icons.Default.AcUnit, R.string.p_conditioner),
                Picture(Icons.Default.Album, R.string.p_album),
                Picture(Icons.Default.Album, R.string.p_album)
            ),
            onGesture = {}
        )
    }
}
