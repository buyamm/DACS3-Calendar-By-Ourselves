package com.example.calendarbyourselvesdacs3.di

import android.content.Context
import com.example.calendarbyourselvesdacs3.data.repository.event.EventRepository
import com.example.calendarbyourselvesdacs3.data.repository.sign_in.GoogleAuthUiClient
import com.example.calendarbyourselvesdacs3.domain.model.calendar.GetMonthDays
import com.google.android.gms.auth.api.identity.Identity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Provides
    @Singleton
    fun provideSignInRepository(@ApplicationContext context: Context): GoogleAuthUiClient {
        return GoogleAuthUiClient(
            context = context,
            oneTapClient = Identity.getSignInClient(context)
        )
    }
}