package com.example.calendarbyourselvesdacs3.domain.model.calendar.extension

import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

fun List<LocalDate>.precedingMonthDays(): List<LocalDate> {
    return if (first().dayOfMonth == 1) {
        emptyList()
    } else {
        val previousMonth = first().monthValue
        takeWhile { it.monthValue == previousMonth }
    }
}

fun List<LocalDate>.succeedingMonthDays(): List<LocalDate> {
    val lastDay = last()
    return if (lastDay == lastDay.with(TemporalAdjusters.lastDayOfMonth())) {
        emptyList()
    } else {
        val nextMonth = lastDay.monthValue
        dropWhile { it.monthValue != nextMonth }
    }
}