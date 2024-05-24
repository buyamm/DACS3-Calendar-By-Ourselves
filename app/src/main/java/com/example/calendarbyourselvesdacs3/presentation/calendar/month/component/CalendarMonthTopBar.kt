package com.example.calendarbyourselvesdacs3.presentation.calendar.month.component

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Restore
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import com.example.calendarbyourselvesdacs3.R
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarMonthTopBar(
    date: LocalDate?,
    showReturnToDate: Boolean,
//    onExportClicked: () -> Unit,
//    onImportClicked: () -> Unit,
    onReturnToDateClicked: () -> Unit,
) {
    TopAppBar(
        title = {
            date?.run {
                DateTitle(date = date)
            } ?: run {
                Text(text = stringResource(id = R.string.app_name))
            }
        },
        actions = {
            if (showReturnToDate) {
                ReturnToDateButton {
                    onReturnToDateClicked()
                }
            }
        },
    )
}

@Composable
private fun ReturnToDateButton(onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
    ) {
        Icon(
            painter = rememberVectorPainter(image = Icons.Rounded.Restore),
            contentDescription = stringResource(id = R.string.calendar_return_to_date),
        )
    }
}

@Composable
private fun DateTitle(date: LocalDate) {
    Row {
        val monthText = date.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
        Text(
            modifier = Modifier.alpha(.65f),
            text = "${date.year} / ",
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(text = monthText)
    }
}