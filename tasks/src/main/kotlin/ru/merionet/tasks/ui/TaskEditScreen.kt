package ru.merionet.tasks.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jetsmarter.composecore.ui.ScreenScaffold
import com.jetsmarter.composecore.ui.pickers.DatePickerModal
import com.jetsmarter.composecore.ui.pickers.TimePickerModal
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import ru.merionet.tasks.R
import ru.merionet.tasks.app.data.AppGesture
import ru.merionet.tasks.app.data.AppUiState
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun TaskEditScreen(state: AppUiState.EditTask, onGesture: (AppGesture) -> Unit) {

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        val currentDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        DatePickerModal(
            onDateSelected = { if (null != it) onGesture(AppGesture.EditTask.DateSelected(it)) },
            onDismiss = { showDatePicker = false },
            selectedDate = state.due?.date,
            yearRange = IntRange(currentDateTime.year, currentDateTime.year + 100),
            selectableDates = currentDateTime.date..currentDateTime.date.plus(DatePeriod(years = 100))
        )
    }

    if (showTimePicker) {
        TimePickerModal(
            onTimeSelected = { onGesture(AppGesture.EditTask.TimeSelected(it)) },
            onDismiss = { showTimePicker = false },
            selectedTime = state.due?.time
        )
    }

    ScreenScaffold(
        title = stringResource(R.string.title_task),
        onBack = { onGesture(AppGesture.Back) },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = state.title,
                        onValueChange = { onGesture(AppGesture.EditTask.TitleChanged(it)) },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(stringResource(R.string.hint_title)) },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = state.description,
                        onValueChange = { onGesture(AppGesture.EditTask.DescriptionChanged(it)) },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(stringResource(R.string.hint_description)) },
                        singleLine = false,
                        minLines = 6,
                        maxLines = 6
                    )

                    Text(
                        text = state.due?.toJavaLocalDateTime()?.let(DUE_DATE_FORMAT::format) ?: stringResource(R.string.hint_no_date),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.large)
                            .padding(24.dp)
                            .clickable { showDatePicker = true }
                    )
                    Text(
                        text = state.due?.toJavaLocalDateTime()?.let(DUE_TIME_FORMAT::format) ?: stringResource(R.string.hint_no_time),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.large)
                            .padding(24.dp)
                            .clickable { showTimePicker = true }
                    )
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (state.completeVisible) {
                        Button(
                            onClick = { onGesture(AppGesture.EditTask.CompleteClicked) },
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(stringResource(R.string.btn_complete))
                        }
                    }
                    Button(
                        onClick = { onGesture(AppGesture.EditTask.SaveClicked) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = state.saveEnabled
                    ) {
                        Text(stringResource(R.string.btn_save))
                    }
                }
            }
        }
    )
}

private val DUE_DATE_FORMAT = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
private val DUE_TIME_FORMAT = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)

@Preview
@Composable
fun TaskEditScreenPreview() {
    val state = AppUiState.EditTask(
        title = "Some title",
        description = "Some description",
        due = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
        completeVisible = true,
        saveEnabled = false
    )
    TaskEditScreen(state) { }
}