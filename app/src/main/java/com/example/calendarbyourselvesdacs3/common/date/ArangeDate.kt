package com.example.calendarbyourselvesdacs3.common.date

import com.example.calendarbyourselvesdacs3.common.room.converter.LocalDateConverter
import java.time.LocalDate

fun getDatesBetween(startDate: LocalDate, endDate: LocalDate): List<LocalDate> {
    val dates = mutableListOf<LocalDate>()
    var currentDate = startDate
    while (currentDate.isBefore(endDate) || currentDate.isEqual(endDate)) {
        dates.add(currentDate)
        currentDate = currentDate.plusDays(1)
    }
    return dates
}

fun listBetweenDates(): String {
    val startDate: LocalDate = LocalDateConverter.toDate("2024-06-14")!!
    val endDate: LocalDate = LocalDateConverter.toDate("2024-06-14")!!

    return getDatesBetween(startDate, endDate).toString()
}