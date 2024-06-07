package com.example.calendarbyourselvesdacs3.presentation.home

import androidx.lifecycle.ViewModel
import com.example.calendarbyourselvesdacs3.data.repository.event.EventRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor (
    private val repository: EventRepository,

) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    private val user: FirebaseUser? = repository.user()

//    fun loadEventsByDate(date: LocalDate) {
//
//            if (user != null) {
//                repository.loadEventByDate(
//                    userId = user.uid,
//                    date = date
//                ) { listEvent ->
//                    _uiState.update {
//                        it.copy(eventList = listEvent)
//                    }
//                }
//            }
//
//    }

    fun deleteEvent(eventId: String) {

            repository.deleteEvent(eventId = eventId) { completed ->
                _uiState.update {
                    it.copy(eventDeletedStatus = completed)
                }
            }

    }


}