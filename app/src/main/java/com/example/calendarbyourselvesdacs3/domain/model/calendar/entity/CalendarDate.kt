package com.example.calendarbyourselvesdacs3.domain.model.calendar.entity

import java.time.LocalDate

data class CalendarDate(
    val date: LocalDate,
    val isPrecedingMonth: Boolean,
    val isSucceedingMonth: Boolean,
)

val CalendarDate.isInMonth get() = !isPrecedingMonth && !isSucceedingMonth
