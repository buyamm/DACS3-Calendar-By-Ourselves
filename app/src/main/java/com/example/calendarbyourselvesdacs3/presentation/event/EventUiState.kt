package com.example.calendarbyourselvesdacs3.presentation.event

import java.time.LocalDate
import java.time.LocalTime

data class EventUiState(
    val title: String = "",
    val description: String = "",
    val colorIndex: Int = 0,
    val startDate: LocalDate? = null,
    val startTime: LocalTime? = null,
    val endDate: LocalDate? = null,
    val endTime: LocalTime? = null,
    val isCheckAllDay: Boolean = false,
    val isCheckNotification: Boolean = false,

    val eventAddedStatus: Boolean = false,
    val eventUpdatedStatus: Boolean = false,
)
