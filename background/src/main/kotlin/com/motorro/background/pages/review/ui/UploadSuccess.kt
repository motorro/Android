package com.motorro.background.pages.review.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.motorro.background.R
import com.motorro.composecore.ui.theme.AppDimens
import com.motorro.composecore.ui.theme.AppTheme

@Composable
fun UploadSuccess(
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().padding(AppDimens.margin_all),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.title_upload_success),
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(AppDimens.spacer))
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = stringResource(R.string.title_uploading_review),
            modifier = Modifier.size(128.dp)
        )
        Spacer(modifier = Modifier.height(AppDimens.spacer))
        Button(
            onClick = onClose,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.btn_close))
        }
        }
}

@Preview
@Composable
private fun UploadSuccessPreview() {
    AppTheme {
        UploadSuccess(onClose = {})
    }
}
