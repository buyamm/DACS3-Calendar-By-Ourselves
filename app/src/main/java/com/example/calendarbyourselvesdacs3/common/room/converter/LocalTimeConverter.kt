package com.example.calendarbyourselvesdacs3.common.room.converter

import androidx.room.TypeConverter
import java.time.LocalDate

object LocalTimeConverter {
    @TypeConverter
    fun toDate(dateString: String?): LocalDate? {
        return if (dateString == null) {
            null
        } else {
            LocalDate.parse(dateString)
        }
    }

    @TypeConverter
    fun toDateString(date: LocalDate?): String? {
        return date?.toString()
    }
}