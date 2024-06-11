package com.example.calendarbyourselvesdacs3.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calendarbyourselvesdacs3.data.repository.event.EventRepository
import com.example.calendarbyourselvesdacs3.domain.model.event.Event
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: EventRepository,

    ) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    private val user: FirebaseUser? = repository.user()
    private var getEventsJob: Job? = null

    fun onChangeDate(date: LocalDate){
        _uiState.update {
            it.copy(
                clickedDate = date
            )
        }
    }

    fun loadEventsByDate(date: LocalDate) {
        if (date != null) {
            getEventsJob?.cancel()

            getEventsJob = viewModelScope.launch {
                if (user != null) {
                    repository.loadEventByDate(
                        userId = user.uid,
                        date = date,
                    ).collect { result ->
                        result?.let {
                            val events = it.data // List<Event>
                            if (events != null) {
                                _uiState.update { homeUiState ->
                                    homeUiState.copy(
                                        eventList = it,
                                        eventQuantity = events.size,
                                    )
                                }
                            } else {
                                _uiState.update { homeUiState ->
                                    homeUiState.copy(
                                        eventList = it,
                                        eventQuantity = 0,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun deleteEvent(event: Event) {
        _uiState.update {
            it.copy(deletedEvent = event)
        }
        repository.deleteEvent(eventId = event.documentId) { completed ->
            _uiState.update {
                it.copy(eventDeletedStatus = completed)
            }
        }
    }

    fun undoDeletedEvent() {
        _uiState.value.deletedEvent?.let {
            repository.addEvent(it) { completed ->
                _uiState.update { homeUiState ->
                    homeUiState.copy(eventUndoDeletedStatus = completed)
                }
            }
        }
    }


    fun getEventQuantity(date: LocalDate): Int{
        var result = 0
        viewModelScope.launch {
            result = repository.countEventQuantityByDate(user!!.uid, date)
        }

        return result
    }

}