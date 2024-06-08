package com.example.calendarbyourselvesdacs3.domain.model.event

import com.google.firebase.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class Event(
    val userId: String? = "",
    val title: String = "",
    val description: String = "",
    val isCheckAllDay: Boolean = false,
    val isCheckNotification: Boolean = false,
    val startDay: Timestamp = Timestamp.now(),
//    val startDate: String = timestampToString(startDay),
    val endDay: Timestamp = Timestamp.now(),
    val colorIndex: Int = 0,
    val documentId: String = ""
)

fun timestampToString(timestamp: Timestamp): String{
    val instant = Instant.ofEpochSecond(timestamp.seconds, timestamp.nanoseconds.toLong())
    val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return localDateTime.format(formatter)
}
