package com.example.calendarbyourselvesdacs3.presentation.home

import com.example.calendarbyourselvesdacs3.domain.model.event.Event

data class HomeUiState(
    val eventList: List<Event> = emptyList(),
    val eventDeletedStatus: Boolean = false
)
