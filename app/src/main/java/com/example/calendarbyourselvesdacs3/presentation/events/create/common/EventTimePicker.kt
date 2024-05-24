package com.example.calendarbyourselvesdacs3.presentation.events.create.common

import androidx.compose.material3.TimePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import com.example.calendarbyourselvesdacs3.presentation.common.date.TimePickerDialog
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePicker(
    onDismissRequest: () -> Unit,
    onUpdateTime: (LocalTime) -> Unit,
) {
    val timePickerState = rememberTimePickerState(is24Hour = true)

    TimePickerDialog(
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            onUpdateTime(
                LocalTime.of(
                    timePickerState.hour,
                    timePickerState.minute,
                ),
            )
            onDismissRequest()
        },
    ) {
        TimePicker(state = timePickerState)
    }
}