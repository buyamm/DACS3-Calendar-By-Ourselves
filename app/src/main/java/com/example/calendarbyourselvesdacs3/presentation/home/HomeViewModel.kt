package com.example.calendarbyourselvesdacs3.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calendarbyourselvesdacs3.data.repository.event.EventRepository
import com.example.calendarbyourselvesdacs3.data.repository.sign_in.GoogleAuthUiClient
import com.example.calendarbyourselvesdacs3.domain.model.user.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: EventRepository,
    private val signInRepository: GoogleAuthUiClient
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    val user: UserData? = signInRepository.getSignedInUser()

    fun loadEventsByDate(date: LocalDate) {
        viewModelScope.launch {
            if (user != null) {
                repository.loadEventByDate(
                    userId = user.userId,
                    date = date
                ) { listEvent ->
                    _uiState.update {
                        it.copy(eventList = listEvent)
                    }
                }
            }
        }
    }

    fun deleteEvent(eventId: String) {
        viewModelScope.launch {
            repository.deleteEvent(eventId = eventId) { completed ->
                _uiState.update {
                    it.copy(eventDeletedStatus = completed)
                }
            }
        }
    }


}