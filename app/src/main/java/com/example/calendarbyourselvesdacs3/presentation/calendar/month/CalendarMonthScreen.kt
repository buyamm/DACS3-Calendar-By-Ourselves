package com.example.calendarbyourselvesdacs3.presentation.calendar.month

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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.calendarbyourselvesdacs3.data.Resource
import com.example.calendarbyourselvesdacs3.domain.model.calendar.entity.MonthDays
import com.example.calendarbyourselvesdacs3.domain.model.user.UserData
import com.example.calendarbyourselvesdacs3.presentation.calendar.month.component.CalendarMonthTopBar
import com.example.calendarbyourselvesdacs3.presentation.calendar.month.component.CalendarView
import com.example.calendarbyourselvesdacs3.presentation.calendar.month.component.NoEventScreen
import com.example.calendarbyourselvesdacs3.presentation.home.HomeViewModel
import com.example.calendarbyourselvesdacs3.presentation.home.component.EventComponentLikeApple
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import java.time.LocalDate


@Composable
fun CalendarMonthScreen(
    viewModel: CalendarMonthViewModel = hiltViewModel(),
    onNavigateCreateEvent: (LocalDate) -> Unit,
    onNavigateDay: (LocalDate) -> Unit,
    onNavigateToUpdateEvent: (eventId: String) -> Unit,
    userData: UserData?,
    onSignOut: () -> Unit,
    onSearchClick: () -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel(),
    darkTheme: Boolean,
    onThemeUpdated: () -> Unit,
) {
    val state by viewModel.collectAsState() // by thì không cần .value
    val uiState = homeViewModel.uiState.collectAsStateWithLifecycle().value

    var date by rememberSaveable {
        mutableStateOf<LocalDate?>(null)
    }

    LaunchedEffect(Unit, date) {
        date?.let {
            homeViewModel.onChangeDate(date = date!!)
            if (userData != null) {
                homeViewModel.loadEventsByDate(date = it, userData)
            }
        }
    }

    viewModel.collectSideEffect {
        when (it) {
            is CalendarMonthViewModel.SideEffect.NavigateCreateEvent -> {
                date = it.date
            }

            is CalendarMonthViewModel.SideEffect.NavigateToDay -> {
                date = it.date
            }

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
                darkTheme = darkTheme,
                onThemeUpdated = onThemeUpdated
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { date?.let { onNavigateCreateEvent(it) } }) {
                Icon(imageVector = Icons.Rounded.Add, contentDescription = null)
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
        ) {
            if (state.monthDays != null) {
                if (userData != null) {
                    Calendar(
                        date = state.currentDate,
                        monthDays = state.monthDays!!,
                        onPreviousMonth = { viewModel.onPreviousMonth() },
                        onNextMonth = { viewModel.onNextMonth() },
                        onDateClicked = { date -> viewModel.onDateClicked(date) },
                        userData = userData
                    )
                }
            }
            Divider(modifier = Modifier.fillMaxWidth(1f), color = Color.LightGray, thickness = 1.dp)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                if (uiState.eventList?.data?.isNotEmpty() == true) {
                    Text(
                        text = "See all events",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Red,
                        modifier = Modifier
                            .padding(top = 12.dp, end = 30.dp, start = 12.dp, bottom = 12.dp)
                            .clickable {
                                uiState.clickedDate?.let { date ->
                                    onNavigateDay(date)
                                }
                            }
                    )
                }
            }
            when (uiState.eventList) {
                is Resource.Error -> {
                    uiState.eventList.throwable?.message?.let { it1 ->
                        Text(
                            text = it1,
                            color = Color.Red
                        )
                    }
                }

                is Resource.Loading -> {
                }

                is Resource.Success -> {

                    if (uiState.eventList?.data?.isNotEmpty() == true) {
                        LazyColumn(
                            contentPadding = PaddingValues(all = 16.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            itemsIndexed(uiState.eventList.data) { index, event ->

                                EventComponentLikeApple(
                                    event = event,
                                    onEventClick = {
                                        onNavigateToUpdateEvent(it)
                                    }
                                )
                                if (index < uiState.eventList.data.size - 1){
                                    Spacer(modifier = Modifier
                                        .fillMaxWidth(1f)
                                        .height(12.dp))
                                    Divider(modifier = Modifier
                                        .fillMaxWidth(1f)
                                        .height(1.dp)
                                    )
                                    Spacer(modifier = Modifier
                                        .fillMaxWidth(1f)
                                        .height(12.dp))
                                }
                            }
                        }
                    } else {

                        if (date == null) {

                            uiState.clickedDate?.let { date ->
                                NoEventScreen() {
                                    onNavigateCreateEvent(date)
                                }
                            }
                        } else {
                            NoEventScreen() {
                                onNavigateCreateEvent(date!!)
                            }
                        }

                    }

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
    userData: UserData
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
        userData = userData
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