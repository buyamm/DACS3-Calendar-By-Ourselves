package com.example.calendarbyourselvesdacs3.domain.model.event

import com.google.firebase.Timestamp

data class Event(
    val userId: String? = "",
    val title: String = "",
    val description: String = "",
    val isCheckAllDay: Boolean = false,
    val isCheckNotification: Boolean = false,
    val startDay: Timestamp = Timestamp.now(),
    val endDay: Timestamp = Timestamp.now(),
    val colorIndex: Int = 0,
    val documentId: String = ""
)
