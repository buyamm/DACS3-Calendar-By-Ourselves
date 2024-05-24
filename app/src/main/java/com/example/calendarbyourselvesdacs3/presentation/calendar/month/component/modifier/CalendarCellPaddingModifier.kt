package com.example.calendarbyourselvesdacs3.presentation.calendar.month.component.modifier

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data object CalendarViewIndices {
    val topEdgeIndices = listOf(0, 1, 2, 3, 4, 5, 6)
    val bottomEdgeIndices = listOf(35, 36, 37, 38, 39, 40, 41)
    val startEdgeIndices = listOf(0, 7, 14, 21, 28, 35)
    val endEdgeIndices = listOf(6, 13, 20, 27, 34, 41)
}

fun Modifier.calendarCellPadding(index: Int): Modifier {
    CalendarViewIndices.run {
        return padding(
            top = if (index in topEdgeIndices) 2.dp else 1.dp,
            bottom = if (index in bottomEdgeIndices) 2.dp else 1.dp,
            start = if (index in startEdgeIndices) 2.dp else 1.dp,
            end = if (index in endEdgeIndices) 2.dp else 1.dp,
        )
    }
}
