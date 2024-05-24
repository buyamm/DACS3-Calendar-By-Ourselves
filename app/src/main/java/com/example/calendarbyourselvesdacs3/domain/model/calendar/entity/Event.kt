package com.example.calendarbyourselvesdacs3.domain.model.calendar.entity

import java.time.LocalDate
import java.time.LocalTime

data class Event(
    val id: Long,
    val title: String,
    val description: String,
    val date: LocalDate,
    val time: LocalTime,
)
