package com.motorro.notifications.pages.notification.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.motorro.composecore.ui.Loading
import com.motorro.composecore.ui.theme.AppDimens
import com.motorro.composecore.ui.theme.AppTheme
import com.motorro.notifications.MyNotificationChannel
import com.motorro.notifications.R
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
private fun NotificationForm(
    state: NotificationViewState.Form,
    onGesture: (NotificationGesture) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(AppDimens.margin_all)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(AppDimens.spacer_small)
    ) {
        Text("Create notification", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = state.data.title,
            onValueChange = { onGesture(NotificationGesture.TitleChanged(it)) },
            label = { Text(stringResource(R.string.edit_title)) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.data.text,
            onValueChange = { onGesture(NotificationGesture.TextChanged(it)) },
            label = { Text(stringResource(R.string.edit_text)) },
            modifier = Modifier.fillMaxWidth()
        )

        ChannelDropdown(
            channels = state.availableChannels,
            selectedChannel = state.data.channel,
            onChannelSelected = { onGesture(NotificationGesture.ChannelChanged(it)) },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { onGesture(NotificationGesture.Send) },
            enabled = state.sendEnabled,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.btn_send))
        }

        Button(
            onClick = { onGesture(NotificationGesture.Dismiss) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.btn_dismiss))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChannelDropdown(
    channels: List<MyNotificationChannel>,
    selectedChannel: MyNotificationChannel,
    onChannelSelected: (MyNotificationChannel) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
            readOnly = true,
            value = stringResource(id = selectedChannel.title),
            onValueChange = {},
            label = { Text(stringResource(R.string.edit_notification_channel)) },
            leadingIcon = { Icon(painter = painterResource(id = selectedChannel.icon), contentDescription = null) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = OutlinedTextFieldDefaults.colors()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            channels.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(stringResource(id = selectionOption.title)) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = selectionOption.icon),
                            contentDescription = null
                        )
                    },
                    onClick = {
                        onChannelSelected(selectionOption)
                        expanded = false
                    }
                )
            }
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
            data = NotificationData(title = "", text = "", channel = MyNotificationChannel.URGENT),
            availableChannels = MyNotificationChannel.entries,
            sendEnabled = false
        ),
        // State with some data, send button is still disabled as one field is empty
        NotificationViewState.Form(
            data = NotificationData(title = "Test Title", text = "", channel = MyNotificationChannel.URGENT),
            availableChannels = MyNotificationChannel.entries,
            sendEnabled = false
        ),
        // State with some data, send button is enabled
        NotificationViewState.Form(
            data = NotificationData(title = "Test Title", text = "Message", channel = MyNotificationChannel.URGENT),
            availableChannels = MyNotificationChannel.entries,
            sendEnabled = true
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


