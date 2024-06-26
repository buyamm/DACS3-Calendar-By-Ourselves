package com.example.calendarbyourselvesdacs3.presentation.calendar.month.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Brightness1
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.calendarbyourselvesdacs3.domain.model.calendar.entity.CalendarDate
import com.example.calendarbyourselvesdacs3.domain.model.calendar.entity.isInMonth
import com.example.calendarbyourselvesdacs3.presentation.calendar.month.component.modifier.calendarCellPadding
import com.example.calendarbyourselvesdacs3.ui.theme.LocalAppColors
import com.google.android.play.integrity.internal.i
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.util.Date
import com.example.calendarbyourselvesdacs3.domain.model.calendar.entity.isInMonth
import com.example.calendarbyourselvesdacs3.domain.model.user.UserData
import com.example.calendarbyourselvesdacs3.presentation.calendar.month.component.modifier.calendarCellPadding
import com.example.calendarbyourselvesdacs3.presentation.home.HomeViewModel
import com.example.calendarbyourselvesdacs3.ui.theme.LocalAppColors

@Composable
fun CalendarCell(
    dateClicked: Boolean,
    onDateClick: () -> Unit,
    index: Int,
    cellSize: Dp,
    isToday: Boolean,
    date: CalendarDate,
    renderCell: @Composable BoxScope.() -> Unit = {},
    onCellClicked: (CalendarDate) -> Unit = {},
    homeViewModel: HomeViewModel = hiltViewModel(),
    userData: UserData
) {
    val appColors = LocalAppColors.current
    var stateColor by remember { mutableStateOf(false) }
    val uiState = homeViewModel.uiState.collectAsStateWithLifecycle().value

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .size(cellSize)
            .calendarCellPadding(index)
            .clip(RoundedCornerShape(2.dp))
            .let { bg ->

                    if (dateClicked) {
                        bg.background(Color.LightGray)
                    } else {
                        if (!date.isInMonth && isToday) {
                            bg
                                .background(appColors.outOfMonthBackground)
                                .graphicsLayer {
                                    alpha = 0.5f
                                }
                        } else if (isToday) {

                            bg.background(appColors.inMonthBackground)

                        } else if (date.isInMonth) {

                            bg.background(appColors.inMonthBackground)

                        } else {
                            bg
                                .background(appColors.outOfMonthBackground)
                                .graphicsLayer {
                                    alpha = 0.5f
                                }
                        }
                    }


            }
            .clickable {
                onCellClicked(date)
                onDateClick()
            }
    ) {

        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(top = 6.dp)
                .height(36.dp)
        ) {
            Text(
                modifier = Modifier.let {

                    if (isToday) {

                        it
                            .background(
                                shape = CircleShape,
                                color = appColors.textColorIsCurrentDay
                            )
                            .size(22.dp)

                    }
                    else {

                        it

                    }

                },
                text = "${date.date.dayOfMonth}",
                color = if (isToday) appColors.calendarContentIsToday else appColors.calendarContent,
                fontSize = 16.sp,
                fontWeight = if (isToday) FontWeight.Bold else FontWeight.W400,
                textAlign = TextAlign.Center
            )
            //hien thi cac dau cham
            if (stateColor) {
                Icon(
                    imageVector = Icons.Rounded.Brightness1,
                    contentDescription = null,
                    tint = Color.Red,
                    modifier = Modifier.size(6.dp)
                )
            }
        }

        LaunchedEffect(Unit, date.date) {

            homeViewModel.onChangeDate(date = date.date)

            var check = homeViewModel.getDateHaveEventVM(userData).find { date.date.toString() == it}

            if(check == date.date.toString()) {
                stateColor = true
            }
            else {
                stateColor = false
            }

        }
    }

}



