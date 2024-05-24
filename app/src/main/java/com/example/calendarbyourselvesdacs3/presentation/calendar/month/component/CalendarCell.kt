package com.example.calendarbyourselvesdacs3.presentation.calendar.month.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calendarbyourselvesdacs3.domain.model.calendar.entity.CalendarDate
import com.example.calendarbyourselvesdacs3.domain.model.calendar.entity.isInMonth
import com.example.calendarbyourselvesdacs3.presentation.calendar.month.component.modifier.calendarCellPadding
import com.example.calendarbyourselvesdacs3.ui.theme.LocalAppColors

@Composable
fun CalendarCell(
    index: Int,
    cellSize: Dp,
    isToday: Boolean,
    date: CalendarDate,
    renderCell: @Composable BoxScope.() -> Unit = {},
    onCellClicked: (CalendarDate) -> Unit = {},
) {
    val appColors = LocalAppColors.current

    Box(
        modifier = Modifier
            .size(cellSize)
            .calendarCellPadding(index)
            .clip(RoundedCornerShape(2.dp))
            .let {
                if (isToday) {
                    it.background(appColors.currentDayBackground)
                } else if (date.isInMonth) {
                    it.background(appColors.inMonthBackground)
                } else {
                    it.background(appColors.outOfMonthBackground)
                }
            }
            .clickable { onCellClicked(date) },
    ) {
        Text(
//            modifier = Modifier.padding(start = 3.dp), //3
            text = "${date.date.dayOfMonth}",
            color = appColors.calendarContent,
            fontSize = 11.sp, //11
        )
        renderCell()
    }
}