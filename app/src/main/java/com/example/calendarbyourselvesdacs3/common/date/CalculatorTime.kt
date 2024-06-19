package com.example.calendarbyourselvesdacs3.common.date

import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun convertToTime(seconds: Long, nanoseconds: Long): String {
    val instant = Instant.ofEpochSecond(seconds, nanoseconds)


    val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())


    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    return localDateTime.format(formatter)
}

// Trả về khoảng cách giữa thời gian hiện tại và ngày cuối của event
fun diffTime(endDateTime: String): String {

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    val nowDateTimeStr = LocalDateTime.now().format(formatter)


    val nowDateTime = LocalDateTime.parse(nowDateTimeStr, formatter)
    val endDateTime = endDateTime

    // Tính toán khoảng cách giữa hai thời điểm
    val duration = Duration.between(nowDateTime, LocalDateTime.parse(endDateTime, formatter))

    val seconds = duration.toMillis()
    return seconds.toString()
}

