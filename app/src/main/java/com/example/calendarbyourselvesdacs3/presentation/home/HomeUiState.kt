package com.example.calendarbyourselvesdacs3.presentation.home

import com.example.calendarbyourselvesdacs3.data.Resource
import com.example.calendarbyourselvesdacs3.domain.model.event.Event
import java.time.LocalDate

data class HomeUiState(
    val eventList: Resource<List<Event>> = Resource.Loading(),
    val eventQuantity: Int = 0,
    val clickedDate: LocalDate? = null,

    val deletedEvent: Event? = null,
    val eventDeletedStatus: Boolean = false,
    val eventUndoDeletedStatus: Boolean = false,
)
