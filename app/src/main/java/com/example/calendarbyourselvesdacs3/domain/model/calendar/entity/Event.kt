package com.example.calendarbyourselvesdacs3.domain.model.calendar.entity

import java.time.LocalDate
import java.time.LocalTime

data class Event(
    val id: String,
    val title: String,
    val description: String,
    val date: LocalDate,
    val time: LocalTime,
)

data class EventsOfDate(
    val title: String,
    val date: String
)
