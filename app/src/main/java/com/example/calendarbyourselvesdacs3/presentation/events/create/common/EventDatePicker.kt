package com.example.calendarbyourselvesdacs3.presentation.events.create.common

import android.R
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import kotlin.time.Duration.Companion.days

@Composable
fun DatePicker(
    initialDate: LocalDate?,
    onDismissRequest: () -> Unit,
    onUpdateDate: (LocalDate) -> Unit,
) {
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialDate?.toEpochDay()?.days?.inWholeMilliseconds,)

    DatePickerDialog(
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let {
                        val date = Instant
                            .ofEpochMilli(it)
                            .atZone(ZoneId.systemDefault()).toLocalDate()

                        onUpdateDate(date)
                    }
                    onDismissRequest()
                },
            ) {
                Text(text = stringResource(id = R.string.ok))
            }
        },
    ) {
        androidx.compose.material3.DatePicker(state = datePickerState)
    }
}
