package com.motorro.composecore.ui.pickers

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.motorro.composecore.R
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TimePickerModal(
    onTimeSelected: (LocalTime) -> Unit,
    onDismiss: () -> Unit,
    selectedTime: LocalTime? = null
) {
    val currentTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).time
    val timePickerState = rememberTimePickerState(
        initialHour = selectedTime?.hour ?: currentTime.hour,
        initialMinute = selectedTime?.minute ?: currentTime.minute
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(stringResource(R.string.btn_cancel))
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onTimeSelected(LocalTime(timePickerState.hour, timePickerState.minute))
                    onDismiss()
                }
            ) {
                Text(stringResource(R.string.btn_ok))
            }
        },
        text = {
            TimePicker(
                state = timePickerState,
            )
        }
    )
}