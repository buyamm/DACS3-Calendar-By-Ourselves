package com.example.calendarbyourselvesdacs3.di

import com.example.calendarbyourselvesdacs3.domain.model.calendar.GetMonthDays
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
class CalendarModule {

    @Provides
    fun getMonthDays() = GetMonthDays()
}