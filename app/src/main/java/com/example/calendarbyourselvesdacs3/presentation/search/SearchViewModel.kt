package com.example.calendarbyourselvesdacs3.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calendarbyourselvesdacs3.data.repository.event.EventRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: EventRepository,
): ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

    private val user: FirebaseUser? = repository.user()
    private var getEventsJob: Job? = null

    fun onQueryChange(searchQuery: String){
        _uiState.update {
            it.copy(searchQuery = searchQuery)
        }
    }

    fun deleteQueryChange(){
        _uiState.update {
            it.copy(searchQuery = "")
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
                    userId = user!!.uid,
                    queryValue = query
                ).collect{
                    _uiState.update {searchUiState ->
                        searchUiState.copy(eventList = it)
                    }
                }

            }
        }
    }


}