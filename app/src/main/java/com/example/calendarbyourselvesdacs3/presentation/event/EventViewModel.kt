package com.example.calendarbyourselvesdacs3.presentation.event

import androidx.lifecycle.ViewModel
import com.example.calendarbyourselvesdacs3.data.repository.event.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val repository: EventRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(EventUiState())
    val uiState = _uiState.asStateFlow()

//    val userId = u

    fun onTitleChange(title: String) {
        _uiState.update {
            it.copy(title = title)
        }
    }

    fun onDescChange(desc: String) {
        _uiState.update {
            it.copy(description = desc)
        }
    }

    fun onColorChange(colorIndex: Int) {
        _uiState.update {
            it.copy(colorIndex = colorIndex)
        }
    }

    fun onStartDateChange(startDate: LocalDate) {
        _uiState.update {
            it.copy(startDate = startDate)
        }
    }

    fun onStartTimeChange(startTime: LocalTime) {
        _uiState.update {
            it.copy(startTime = startTime)
        }
    }

    fun onEndDateChange(endDate: LocalDate) {
        _uiState.update {
            it.copy(endDate = endDate)
        }
    }

    fun onEndTimeChange(endTime: LocalTime) {
        _uiState.update {
            it.copy(endTime = endTime)
        }
    }

    fun onCheckAllDayChange(isCheckAllDay: Boolean) {
        _uiState.update {
            it.copy(isCheckAllDay = isCheckAllDay)
        }
    }

    fun onCheckNotiChange(isCheckNoti: Boolean) {
        _uiState.update {
            it.copy(isCheckNotification = isCheckNoti)
        }
    }

//    fun addEvent(){
//        var event = Event(userId = UserData.)
//        repository.addEvent()
//    }
}