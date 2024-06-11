package com.example.calendarbyourselvesdacs3.presentation.calendar.month

import androidx.lifecycle.ViewModel
import com.example.calendarbyourselvesdacs3.domain.model.calendar.GetMonthDays
import com.example.calendarbyourselvesdacs3.domain.model.calendar.entity.MonthDays
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CalendarMonthViewModel @Inject constructor(
    dateProvider: () -> LocalDate,
    private val getMonthDays: GetMonthDays,
) : ViewModel(), ContainerHost<CalendarMonthViewModel.State, CalendarMonthViewModel.SideEffect> {

    override val container = container<State, SideEffect>(
        initialState = State(currentDate = dateProvider()),
        onCreate = {
            intent {
                updateMonth(state.currentDate)
            }
        },
    )

//    fun scrollMonth(page: Long) {
//        intent {
//            val currentPage = state.currentPage
//            val pageDifference = page - currentPage
//
//            if (pageDifference != 0L) {
//                val date = state.currentDate
//                updateMonth2(date.plusMonths(pageDifference))
//                reduce {
//                    state.copy(currentPage = page)
//                }
//            }
//        }
//    }
//
//
//    context(SimpleSyntax<State, SideEffect>)
//    private suspend fun updateMonth2(date: LocalDate) {
//        val newMonthDays = getMonthDays(date)
//        reduce {
//            state.copy(
//                monthDays = newMonthDays,
//                calendarDate = date,
//            )
//        }
//    }


    fun onNextMonth() {
        intent {
            val date = state.calendarDate
            updateMonth(date.plusMonths(1))
        }
    }


    fun onPreviousMonth() {
        intent {
            val date = state.calendarDate
            updateMonth(date.minusMonths(1))
        }
    }

    fun onResetCalendarDate() {
        intent {
            updateMonth(date = state.currentDate)
        }
    }

//    ========= show date when clicked =============
    fun onDateClicked(date: LocalDate) {

//        Log.d("Test =============>>>>", date.toString())

        intent {
            if ((state.events[date] ?: 0) > 0) {
//                postSideEffect(SideEffect.NavigateToDay(date))
            } else {
                postSideEffect(SideEffect.NavigateCreateEvent(date))
            }
        }
    }

    context(SimpleSyntax<State, SideEffect>)
    private suspend fun updateMonth(date: LocalDate) {
        val newMonthDays = getMonthDays(date)

        reduce {
            state.copy(
                monthDays = newMonthDays,
                calendarDate = date,
            )
        }
    }



    data class State(
        val currentDate: LocalDate,
        val calendarDate: LocalDate = currentDate,
        val monthDays: MonthDays? = null,
        val events: Map<LocalDate, Int> = emptyMap(),
        val currentPage: Long = 0L
    )

    sealed interface SideEffect {
        data class NavigateCreateEvent(
            val date: LocalDate,
        ) : SideEffect

        data class NavigateToDay(
            val date: LocalDate,
        ) : SideEffect
    }
}