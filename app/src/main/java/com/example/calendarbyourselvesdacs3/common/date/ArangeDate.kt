package com.example.calendarbyourselvesdacs3.common.date

import com.example.calendarbyourselvesdacs3.common.room.converter.LocalDateConverter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

fun getDatesBetween(startDate: LocalDate, endDate: LocalDate): List<LocalDate> {
    val dates = mutableListOf<LocalDate>()
    var currentDate = startDate
    while (currentDate.isBefore(endDate) || currentDate.isEqual(endDate)) {
        dates.add(currentDate)
        currentDate = currentDate.plusDays(1)
    }
    return dates
}

fun listDatesWithStartTime(startDateTimeStr: String, endDateTimeStr: String): List<String> {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    // Chuyển đổi chuỗi thời gian thành LocalDateTime
    val startDateTime = LocalDateTime.parse(startDateTimeStr, formatter)
    val endDateTime = LocalDateTime.parse(endDateTimeStr, formatter)

    // Lấy thời gian của ngày bắt đầu
    val startTime = startDateTime.toLocalTime()

    val dates = mutableListOf<String>()
    var currentDate = startDateTime.toLocalDate()

    while (!currentDate.isAfter(endDateTime.toLocalDate())) {
        // Tạo LocalDateTime với ngày hiện tại và thời gian bắt đầu
        val dateTimeWithStartTime = LocalDateTime.of(currentDate, startTime)
        // Thêm vào danh sách nếu thời gian trong khoảng
        if (!dateTimeWithStartTime.isAfter(endDateTime)) {
            dates.add(dateTimeWithStartTime.format(formatter))
        }
        // Tăng currentDate lên một ngày
        currentDate = currentDate.plus(1, ChronoUnit.DAYS)
    }

    return dates
}


