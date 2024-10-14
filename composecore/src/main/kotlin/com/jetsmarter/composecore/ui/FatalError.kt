package com.jetsmarter.composecore.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jetsmarter.composecore.R

@Composable
fun FatalError(
    errorMessage: String?,
    onDismiss: () -> Unit,
    retriable: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(30.dp, Alignment.CenterVertically)
    ) {
        Icon(modifier = Modifier.size(128.dp), tint = MaterialTheme.colorScheme.error, imageVector = Icons.Filled.Warning, contentDescription = "Warning")
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = errorMessage ?: stringResource(R.string.msg_default_error),
            style = MaterialTheme.typography.bodyLarge
        )
        Button(onClick = onDismiss) {
            Text(
                text = if(retriable) {
                    stringResource(R.string.btn_retry)
                } else {
                    stringResource(R.string.btn_dismiss)
                }
            )
        }
    }
}

@Composable
fun FatalErrorScreen(error: String, retriable: Boolean, onDismiss: () -> Unit, onBack: () -> Unit) {
    ScreenScaffold(
        title = stringResource(R.string.title_error),
        onBack = onBack,
        content = { FatalError(error, onDismiss, retriable, it) }
    )
}
