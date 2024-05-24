package com.example.calendarbyourselvesdacs3.presentation.calendar.month.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calendarbyourselvesdacs3.presentation.calendar.month.component.modifier.CalendarLayout
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

context(CalendarLayout)
@Composable
fun WeekDays(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = cellSize / 2)
            .alpha(.6f),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        DayOfWeek.values().forEach { day ->
            Text(
                modifier = Modifier.width(10.dp),
                text = day.getDisplayName(TextStyle.NARROW, Locale.getDefault()),
                fontSize = 12.sp,
            )
        }
    }
}