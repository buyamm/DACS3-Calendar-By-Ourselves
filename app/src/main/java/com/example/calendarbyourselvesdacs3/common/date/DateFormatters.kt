package com.example.calendarbyourselvesdacs3.common.date

import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.TextStyle
import java.time.temporal.ChronoField

val dateFormatter: DateTimeFormatter = DateTimeFormatterBuilder()
    .appendValue(ChronoField.DAY_OF_MONTH, 2)
    .appendLiteral(" ")
    .appendText(ChronoField.MONTH_OF_YEAR, TextStyle.FULL)
    .appendLiteral(" ")
    .appendValue(ChronoField.YEAR, 4)
    .toFormatter()

val timeFormatter: DateTimeFormatter = DateTimeFormatterBuilder()
    .appendValue(ChronoField.HOUR_OF_DAY, 2)
    .appendLiteral(":")
    .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
    .toFormatter()