package com.example.calendarbyourselvesdacs3.presentation.calendar.month

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.calendarbyourselvesdacs3.data.api.MockApi
import com.example.calendarbyourselvesdacs3.domain.model.calendar.entity.MonthDays
import com.example.calendarbyourselvesdacs3.domain.model.calendar.entity.isInMonth
import com.example.calendarbyourselvesdacs3.domain.model.user.UserData
import com.example.calendarbyourselvesdacs3.presentation.calendar.month.component.CalendarMonthTopBar
import com.example.calendarbyourselvesdacs3.presentation.calendar.month.component.CalendarView
import com.example.calendarbyourselvesdacs3.presentation.event.EventWithoutDescriptionComponent
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import java.time.LocalDate

@OptIn(ExperimentalPagerApi::class, ExperimentalFoundationApi::class)
@Composable
fun CalendarMonthScreen(
    viewModel: CalendarMonthViewModel = hiltViewModel(),
    onNavigateCreateEvent: (LocalDate) -> Unit,
    onNavigateDay: (LocalDate) -> Unit,
    userData: UserData?,
    onSignOut: () -> Unit,
    onSearchClick: () -> Unit
) {
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect {
        when (it) {
            is CalendarMonthViewModel.SideEffect.NavigateCreateEvent -> onNavigateCreateEvent(it.date)
            is CalendarMonthViewModel.SideEffect.NavigateToDay -> onNavigateDay(it.date)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CalendarMonthTopBar(
                date = state.calendarDate,
                showReturnToDate = state.currentDate != state.calendarDate,
                onReturnToDateClicked = {
                    viewModel.onResetCalendarDate()
                },
                userData = userData,
                onSignOut = onSignOut,
                onSearchClick = onSearchClick,
            )
        },
    ) {
        Column(
            modifier = Modifier.padding(it),
        ) {
            if (state.monthDays != null) {
                //Test
//                val pagerState = rememberPagerState()
//                HorizontalPager(
//                    count = Int.MAX_VALUE,
//                    state = pagerState,
//                    modifier = Modifier.fillMaxWidth()
//                ) {page ->
//                    viewModel.scrollMonth(page.toLong())
//                    Calendar(
//                        date = state.currentDate,
//                        monthDays = state.monthDays!!,
//                        onPreviousMonth = { viewModel.onPreviousMonth() },
//                        onNextMonth = { viewModel.onNextMonth() },
//                        onDateClicked = { date -> viewModel.onDateClicked(date) },
//                    )
//
//
//                }

//                ===================================

                Calendar(
                    date = state.currentDate,
                    monthDays = state.monthDays!!,
                    onPreviousMonth = { viewModel.onPreviousMonth() },
                    onNextMonth = { viewModel.onNextMonth() },
                    onDateClicked = { date -> viewModel.onDateClicked(date) },
                )
            }
            Divider(modifier = Modifier.fillMaxWidth(1f), color = Color.LightGray, thickness = 1.dp)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Text(
                    text = "See all events",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Red,
                    modifier = Modifier
                        .padding(top = 12.dp, end = 30.dp, start = 12.dp, bottom = 12.dp)
                        .clickable { })
            }
            LazyColumn(contentPadding = PaddingValues(start = 16.dp, top = 16.dp, bottom = 16.dp)) {
                items(MockApi.taskList) { task ->
                    EventWithoutDescriptionComponent(task = task, onEventClick = {})
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun Calendar(
    date: LocalDate,
    monthDays: MonthDays,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onDateClicked: (LocalDate) -> Unit,
) {
    CalendarView(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        monthDays = monthDays,
        date = date,
        onCellClicked = { cellDate -> onDateClicked(cellDate.date) },
        renderCell = { cellDate ->
        },
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        TextButton(
            onClick = { onPreviousMonth() },
        ) {
            Text(text = "Previous month")
        }
        TextButton(
            onClick = { onNextMonth() },
        ) {
            Text(text = "Next month")
        }
    }
}