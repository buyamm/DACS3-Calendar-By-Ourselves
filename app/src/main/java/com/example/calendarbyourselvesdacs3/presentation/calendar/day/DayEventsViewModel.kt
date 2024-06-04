package com.example.calendarbyourselvesdacs3.presentation.calendar.day

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.calendarbyourselvesdacs3.domain.model.calendar.entity.Event
import com.example.calendarbyourselvesdacs3.presentation.navigation.localDateArg
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription
import org.orbitmvi.orbit.viewmodel.container
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class DayEventsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
//    private val repository: EventsRepository,
) : ViewModel(), ContainerHost<DayEventsViewModel.State, DayEventsViewModel.SideEffect> {

    override val container: Container<State, SideEffect> = container(
        initialState = State(),
        onCreate = {
            val date = savedStateHandle.get<String>("date")!!.localDateArg()

            reduce {
                state.copy(
                    date = date,
                )
            }

            repeatOnSubscription {
                loadEvents(date)
            }
        },
    )

    fun onAddEvent() {
        intent {
            val date = state.date ?: return@intent

            postSideEffect(SideEffect.NavigateCreateEvent(date))
        }
    }

    fun onDeleteEvent(event: Event) {
        intent {
            println(event.id)
//            repository.deleteEvent(event.id)

            val date = state.date ?: return@intent
            loadEvents(date)
        }
    }

    //    =============== hiển thị sự kiện trong ngày ===================
    private suspend fun loadEvents(date: LocalDate) {
//        val events = repository.findByDate(date)
//        reduce {
//            state.copy(
//                events = events,
//            )
//        }
    }

    data class State(
        val date: LocalDate? = null,
        val events: List<Event> = emptyList(),
    )

    sealed interface SideEffect {
        data class NavigateCreateEvent(val date: LocalDate) : SideEffect
    }
}