package com.example.calendarbyourselvesdacs3.presentation.search

import com.example.calendarbyourselvesdacs3.data.Resource
import com.example.calendarbyourselvesdacs3.domain.model.event.Event

data class SearchUiState(
    val eventList: Resource<List<Event>> = Resource.Loading(),
    val searchQuery: String = ""
)
