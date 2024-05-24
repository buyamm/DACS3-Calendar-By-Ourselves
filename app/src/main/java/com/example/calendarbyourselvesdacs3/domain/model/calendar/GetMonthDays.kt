package com.example.calendarbyourselvesdacs3.domain.model.calendar

import com.example.calendarbyourselvesdacs3.domain.model.calendar.entity.CalendarDate
import com.example.calendarbyourselvesdacs3.domain.model.calendar.entity.MonthDays
import com.example.calendarbyourselvesdacs3.domain.model.calendar.extension.precedingMonthDays
import com.example.calendarbyourselvesdacs3.domain.model.calendar.extension.succeedingMonthDays
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

class GetMonthDays {

    operator fun invoke(date: LocalDate): MonthDays {
        return createCalendarDays(date)
    }

    private fun createCalendarDays(date: LocalDate): List<CalendarDate> {
        val start = date
            .with(TemporalAdjusters.firstDayOfMonth())
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

        val days = buildList {
            add(start)

            1.until(42).onEach {
                add(start.plusDays(it.toLong()))
            }
        }

        val precedingDays = days.precedingMonthDays()
        val succeedingDays = days.succeedingMonthDays()

        return days.map {
            CalendarDate(
                date = it,
                isPrecedingMonth = it in precedingDays,
                isSucceedingMonth = it in succeedingDays,
            )
        }
    }
}