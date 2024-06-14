package com.example.calendarbyourselvesdacs3.common.date

import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.time.Duration.Companion.seconds

fun convertToTime(seconds: Long, nanoseconds: Long): String {
    // Tạo đối tượng Instant từ seconds và nanoseconds
    val instant = Instant.ofEpochSecond(seconds, nanoseconds)

    // Chuyển đổi Instant sang LocalDateTime với múi giờ hệ thống mặc định
    val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())

    // Định dạng LocalDateTime thành chuỗi
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    return localDateTime.format(formatter)
}

fun diffTime(endDateTime: String): String {


    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    // Định nghĩa các thời điểm
    val nowDateTimeStr = LocalDateTime.now().format(formatter)

    // Chuyển đổi chuỗi thời gian sang LocalDateTime
    val nowDateTime = LocalDateTime.parse(nowDateTimeStr, formatter)
    val endDateTime = endDateTime

    // Tính toán khoảng cách giữa hai thời điểm
    val duration = Duration.between(nowDateTime, LocalDateTime.parse(endDateTime, formatter))

    // Đổi khoảng cách sang mili giây
    val seconds = duration.toMillis()
    return seconds.toString()
}

