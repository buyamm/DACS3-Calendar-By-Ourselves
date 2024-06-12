package com.example.calendarbyourselvesdacs3.presentation.event

import com.example.calendarbyourselvesdacs3.data.Resource
import com.example.calendarbyourselvesdacs3.domain.model.user.User
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

    val userList: Resource<List<User>> = Resource.Loading(),
    val selectedUserList: List<User> = emptyList(),
    val searchQuery: String = "",

    val eventAddedStatus: Boolean = false,
    val eventUpdatedStatus: Boolean = false,
)
