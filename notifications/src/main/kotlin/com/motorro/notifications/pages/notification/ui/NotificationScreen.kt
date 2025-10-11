package com.motorro.notifications.pages.notification.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.motorro.composecore.ui.Loading
import com.motorro.composecore.ui.theme.AppDimens
import com.motorro.composecore.ui.theme.AppTheme
import com.motorro.notifications.pages.notification.data.NotificationData
import com.motorro.notifications.pages.notification.data.NotificationGesture
import com.motorro.notifications.pages.notification.data.NotificationViewState

@Composable
fun BasicNotificationScreen(state: NotificationViewState, onGesture: (NotificationGesture) -> Unit, modifier: Modifier = Modifier) {
    when (state) {
        NotificationViewState.Loading -> Loading(modifier)
        is NotificationViewState.Form -> NotificationForm(state, onGesture, modifier)
    }
}

@Composable
fun NotificationForm(
    state: NotificationViewState.Form,
    onGesture: (NotificationGesture) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(AppDimens.margin_all).fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(AppDimens.spacer_small)
    ) {
        Text("Create notification", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = state.data.title,
            onValueChange = { onGesture(NotificationGesture.TitleChanged(it)) },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.data.text,
            onValueChange = { onGesture(NotificationGesture.TextChanged(it)) },
            label = { Text("Text") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { onGesture(NotificationGesture.Send) },
            enabled = state.sendEnabled,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Send")
        }
    }
}

/**
 * A provider for generating different states of the `NotificationForm` for previews.
 * This allows showcasing the component with various data and enabled states.
 */
class NotificationFormStateProvider : PreviewParameterProvider<NotificationViewState.Form> {
    override val values: Sequence<NotificationViewState.Form> = sequenceOf(
        // State with empty fields, send button is disabled
        NotificationViewState.Form(
            data = NotificationData(title = "", text = ""),
            sendEnabled = false
        ),
        // State with some data, send button is still disabled as one field is empty
        NotificationViewState.Form(
            data = NotificationData(title = "Test Title", text = ""),
            sendEnabled = false
        )
    )
}

/**
 * Composable preview for the NotificationForm.
 * Uses a [PreviewParameterProvider] to display the form in multiple states.
 */
@Preview
@Composable
private fun NotificationFormPreview(@PreviewParameter(NotificationFormStateProvider::class) state: NotificationViewState.Form) {
    AppTheme {
        NotificationForm(
            state = state,
            onGesture = {}
        )
    }
}


