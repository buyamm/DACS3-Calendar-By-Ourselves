package com.example.calendarbyourselvesdacs3.presentation.events.create.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import com.example.calendarbyourselvesdacs3.common.date.timeFormatter
import java.time.LocalTime

@Composable
fun TimeField(
    time: LocalTime,
    onShowTimePicker: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = timeFormatter.format(time),
        )
        IconButton(
            onClick = { onShowTimePicker() },
        ) {
            Icon(
                painter = rememberVectorPainter(Icons.Default.AccessTime),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    }
}