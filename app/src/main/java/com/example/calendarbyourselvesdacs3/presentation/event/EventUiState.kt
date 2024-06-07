package com.example.calendarbyourselvesdacs3.presentation.event

import java.time.LocalDate
import java.time.LocalTime

data class EventUiState(
    val title: String = "",
    val description: String = "",
    val colorIndex: Int = 0,
    val startDate: LocalDate = LocalDate.now(),
    val startTime: LocalTime = LocalTime.now(),
    val endDate: LocalDate = LocalDate.now(),
    val endTime: LocalTime = LocalTime.now().plusHours(1),
    val isCheckAllDay: Boolean = false,
    val isCheckNotification: Boolean = false,

    val eventAddedStatus: Boolean = false,
    val eventUpdatedStatus: Boolean = false,
)
