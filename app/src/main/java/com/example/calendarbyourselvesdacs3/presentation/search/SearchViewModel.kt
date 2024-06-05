package com.example.calendarbyourselvesdacs3.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calendarbyourselvesdacs3.data.repository.event.EventRepository
import com.example.calendarbyourselvesdacs3.data.repository.sign_in.GoogleAuthUiClient
import com.example.calendarbyourselvesdacs3.domain.model.user.UserData
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: EventRepository,
    private val signInRepository: GoogleAuthUiClient
): ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

    val user: UserData? = signInRepository.getSignedInUser()
    private var getEventsJob: Job? = null

    fun onQueryChange(searchQuery: String){
        _uiState.update {
            it.copy(searchQuery = searchQuery)
        }
    }

    fun updateEventListSearch(query: String){
        loadEventBySearch(query)
    }

    private fun loadEventBySearch(query: String = ""){
        _uiState.update {
            it.copy(searchQuery = query)
        }

        if(query.isNotEmpty()){
            getEventsJob?.cancel()

            getEventsJob = viewModelScope.launch {
                repository.loadEventBySearch(
                    userId = user!!.userId,
                    queryValue = query
                ){listEvent ->
                    _uiState.update {
                        it.copy(eventList = listEvent)
                    }
                }
            }
        }
    }


}