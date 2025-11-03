package com.motorro.background.pages.review.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.zIndex
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.motorro.background.R
import com.motorro.background.pages.review.data.Photo
import com.motorro.background.pages.review.data.ReviewData
import com.motorro.background.pages.review.data.ReviewGesture
import com.motorro.background.pages.review.data.ReviewUiState
import com.motorro.composecore.ui.theme.AppDimens
import com.motorro.composecore.ui.theme.AppTheme
import java.io.File

@Composable
fun ReviewForm(
    state: ReviewUiState.Form,
    onGesture: (ReviewGesture) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val (picUri, setPicUri) = remember { mutableStateOf(Uri.EMPTY) }

    val takePicture = rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicture()) { saved ->
        if (saved) {
            onGesture(ReviewGesture.PhotoAttached(Photo(picUri)))
        } else {
            context.contentResolver.delete(picUri, null, null)
            setPicUri(Uri.EMPTY)
        }
    }

    fun takePhoto() {
        val tempFile = File.createTempFile(
            "temp_image",
            ".jpg",
            context.cacheDir
        )
        val uri = FileProvider.getUriForFile(
            context,
            context.getString(R.string.files_authority),
            tempFile
        )
        setPicUri(uri)
        takePicture.launch(uri)
    }

    Column(
        modifier = modifier.fillMaxSize().padding(AppDimens.margin_all),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.page_review),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = AppDimens.spacer)
        )
        Rating(
            rating = state.data.rating,
            maxRating = 5,
            onChange = { onGesture(ReviewGesture.RatingChanged(it)) }
        )
        OutlinedTextField(
            value = state.data.text,
            onValueChange = { onGesture(ReviewGesture.TextChanged(it)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = AppDimens.spacer),
            label = { Text(stringResource(R.string.input_review)) },
            minLines = 5,
            maxLines = 10,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text, imeAction = ImeAction.Default)
        )
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = AppDimens.spacer)
        ) {
            item {
                Card(
                    modifier = Modifier
                        .size(AppDimens.list_image_size)
                        .clip(MaterialTheme.shapes.medium)
                ) {
                    IconButton(
                        onClick = { takePhoto() },
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = stringResource(R.string.btn_add_photo))
                    }
                }
            }
            items(state.data.photos) { photo ->
                PhotoAttachment(
                    photo = photo,
                    onDelete = { onGesture(ReviewGesture.PhotoRemoved(it)) }
                )
            }
        }
        Button(
            onClick = { onGesture(ReviewGesture.Action) },
            enabled = state.submitEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = AppDimens.spacer)
        ) {
            Text(stringResource(R.string.btn_submit_review))
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun PhotoAttachment(
    photo: Photo,
    onDelete: (Photo) -> Unit
) {
    Box(
        modifier = Modifier
            .size(AppDimens.list_image_size)
            .padding(start = AppDimens.margin_all_small)
            .clip(MaterialTheme.shapes.medium)
    ) {
        GlideImage(
            model = photo.uri,
            contentScale = ContentScale.Crop,
            contentDescription = stringResource(R.string.desc_attachment),
            modifier = Modifier.matchParentSize().zIndex(1F)
        )
        IconButton(
            onClick = { onDelete(photo) },
            modifier = Modifier.align(Alignment.TopEnd).zIndex(2f)
        ) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = stringResource(R.string.btn_delete_photo))
        }
    }
}

private class ReviewFormProvider : PreviewParameterProvider<ReviewUiState.Form> {
    override val values: Sequence<ReviewUiState.Form> = sequenceOf(
        ReviewUiState.Form(ReviewData(), false),
        ReviewUiState.Form(
            ReviewData(
                rating = 4,
                text = "Some review",
                photos = listOf(
                    Photo("https://picsum.photos/id/237/200/300".toUri()),
                    Photo("https://picsum.photos/id/238/200/300".toUri())
                )
            ),
            true
        )
    )
}

@Preview
@Composable
private fun ReviewFormPreview(@PreviewParameter(ReviewFormProvider::class) state: ReviewUiState.Form) {
    AppTheme {
        ReviewForm(state = state, onGesture = {})
    }
}
