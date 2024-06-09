package com.example.calendarbyourselvesdacs3.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calendarbyourselvesdacs3.data.repository.event.EventRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor (
    private val repository: EventRepository,

) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    private val user: FirebaseUser? = repository.user()

//    fun onChangeEventQuantity(eventList: List<Event>){
//        _uiState.update {
//            it.copy(
//                eventQuantity = eventList.size
//            )
//        }
//    }
    fun loadEventsByDate(date: LocalDate) {
        viewModelScope.launch {
            if (user != null) {
                repository.loadEventByDate(
                    userId = user.uid,
                    date = date
                ).collect{ result ->
                    result?.let {
                        val data = it.data // List<Event>
                        if(data != null){
                            _uiState.update {homeUiState ->
                                homeUiState.copy(
                                    eventList = it,
                                    eventQuantity = data.size,
                                )
                            }
                        }else{
                            _uiState.update {homeUiState ->
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

    fun deleteEvent(eventId: String) {
            repository.deleteEvent(eventId = eventId) { completed ->
                _uiState.update {
                    it.copy(eventDeletedStatus = completed)
                }
            }
    }





}