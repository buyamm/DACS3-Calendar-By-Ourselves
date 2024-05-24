package com.example.calendarbyourselvesdacs3.di;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent::class)
class CalendarModule {

    @Provides
    fun getMonthDays() = GetMonthDays()
}
