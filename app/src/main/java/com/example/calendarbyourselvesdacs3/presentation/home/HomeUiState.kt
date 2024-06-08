package com.example.calendarbyourselvesdacs3.presentation.home

import com.example.calendarbyourselvesdacs3.data.Resource
import com.example.calendarbyourselvesdacs3.domain.model.event.Event

data class HomeUiState(
    val eventList: Resource<List<Event>> = Resource.Loading(),
    val eventQuantity: Int = 0,

    val eventDeletedStatus: Boolean = false
)
