package com.jetsmarter.composecore.ui.pickers

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.jetsmarter.composecore.R
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DatePickerModal(
    onDateSelected: (LocalDate?) -> Unit,
    onDismiss: () -> Unit,
    selectedDate: LocalDate? = null,
    yearRange: IntRange = DatePickerDefaults.YearRange,
    selectableDates: ClosedRange<LocalDate>? = null
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate?.let {
            LocalDateTime(it, LocalTime.fromSecondOfDay(0)).toEpochMillis()
        },
        yearRange = yearRange,
        selectableDates = selectableDates?.toSelectableDates() ?: DatePickerDefaults.AllDates
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(
                    datePickerState.selectedDateMillis?.let {
                        Instant.fromEpochMilliseconds(it).toLocalDateTime(TimeZone.currentSystemDefault()).date
                    }
                )
                onDismiss()
            }) {
                Text(stringResource(R.string.btn_ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.btn_cancel))
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
private fun ClosedRange<LocalDate>.toSelectableDates() = object : SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        return utcTimeMillis.toLocalDateTime().let { it.date in start..endInclusive }
    }
    override fun isSelectableYear(year: Int): Boolean {
        return year >= start.year && year <= endInclusive.year
    }
}