package com.example.calendarbyourselvesdacs3.di

import com.example.calendarbyourselvesdacs3.data.repository.event.EventRepository
import com.example.calendarbyourselvesdacs3.domain.model.calendar.GetMonthDays
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class CalendarModule {

    @Provides
    fun getMonthDays() = GetMonthDays()

    @Provides
    @Singleton
    fun provideEventRepository(): EventRepository {
        return EventRepository()
    }



//    @Provides
//    @Singleton
//    fun provideSignInRepository(@ApplicationContext context: Context): GoogleAuthUiClient {
//        return GoogleAuthUiClient(
//            context = context,
//            oneTapClient = Identity.getSignInClient(context)
//        )
//    }
}