package com.example.calendarbyourselvesdacs3.common

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.LocalDate

@Module
@InstallIn(SingletonComponent::class)
class CommonModule {

    @Provides
    fun dateProvider(): () -> LocalDate = { LocalDate.now() }
}