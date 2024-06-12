package com.example.calendarbyourselvesdacs3.domain.model.event

import com.google.firebase.Timestamp
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class Event(
    val userId: String? = "",
    val title: String = "New event",
    val description: String = "",
    val checkAllDay: Boolean = false,
    val checkNotification: Boolean = false,
    val startDay: Timestamp = Timestamp.now(),
    val endDay: Timestamp = Timestamp.now(),
    val colorIndex: Int = 0,
    val documentId: String = "",
    val startDate: String = timestampToString(startDay),
    val endDate: String = timestampToString(endDay),
    val guest: Array<String> = emptyArray()

)


data class DottedEvent(
    val startDate: String? = "",
    val endDate: String? = ""
)

fun timestampToString(timestamp: Timestamp): String{
    val instant = Instant.ofEpochSecond(timestamp.seconds, timestamp.nanoseconds.toLong())
    val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return localDateTime.format(formatter)
}

fun formatDateToString(timestamp: Timestamp): String{
    val localDateTime = timestamp.toDate().toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()

    val formatter = DateTimeFormatter.ofPattern("MMM dd yyy")
    return localDateTime.format(formatter)
}

fun formatTimeToString(timestamp: Timestamp): String{
    val localDateTime = timestamp.toDate().toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime()

    val formatter = DateTimeFormatter.ofPattern("hh:mm a")
    return localDateTime.format(formatter)
}

fun splitFormattedDate(date: String): List<String>{
    return date.split(" ")
}

fun localDateToString(localDate: LocalDate): String{
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return localDate.format(formatter)
}

fun getDayOfWeek(date: LocalDate): String {
    val day = date.dayOfWeek.toString().toLowerCase()

    return day.replaceFirstChar { it.toUpperCase() }
}

fun stringToLocalTime(s: String): LocalTime {
    val formatter = DateTimeFormatter.ofPattern("hh:mm a")

    return LocalTime.parse(s, formatter)
}