package com.example.calendarbyourselvesdacs3.presentation.calendar.month.component

import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.calendarbyourselvesdacs3.domain.model.calendar.entity.CalendarDate
import com.example.calendarbyourselvesdacs3.domain.model.calendar.entity.MonthDays
import com.example.calendarbyourselvesdacs3.presentation.calendar.month.component.modifier.CalendarLayout
import com.example.calendarbyourselvesdacs3.presentation.calendar.month.component.modifier.calendarLayout
import com.example.calendarbyourselvesdacs3.ui.theme.LocalAppColors
import com.google.android.play.integrity.internal.i
import java.time.LocalDate

@Composable
fun CalendarView(
    modifier: Modifier = Modifier,
    monthDays: MonthDays,
    date: LocalDate,
    renderCell: @Composable BoxScope.(CalendarDate) -> Unit = {},
    onCellClicked: (CalendarDate) -> Unit = {},
) {
    var calendarLayout by remember { mutableStateOf<CalendarLayout?>(null) }
    val appColors = LocalAppColors.current

    //hien thu ngay trong tuan
    LazyColumn {
        item {
            calendarLayout?.run {
                WeekDays(
                    modifier = Modifier
                        .padding(
                            horizontal = 0.dp,
                            vertical = 16.dp,
                        ),
                )
            }

            //hien thi các ngày trong tháng
            FlowRow(
                modifier = modifier
                    .calendarLayout { calendarLayout = it }
                    .clip(RoundedCornerShape(3.dp))
                    .background(appColors.calendarBackground)
                    .padding(start = 1.dp),
            ) {
                calendarLayout?.run {
                    monthDays.forEachIndexed { i, calendarDate ->
                        CalendarCell(
                            index = i,
                            cellSize = cellSize,
                            date = calendarDate,
                            isToday = calendarDate.date == date,
                            onCellClicked = onCellClicked,
                            renderCell = {
                                renderCell(calendarDate)
                            },
                        )
                    }
                }
            }
        }
    }
}