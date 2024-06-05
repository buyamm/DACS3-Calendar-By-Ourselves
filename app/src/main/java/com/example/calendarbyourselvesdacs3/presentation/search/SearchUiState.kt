package com.example.calendarbyourselvesdacs3.presentation.search

import com.example.calendarbyourselvesdacs3.domain.model.event.Event

data class SearchUiState(
    val eventList: List<Event> = emptyList(),
    val searchQuery: String = ""
)
