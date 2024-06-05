package com.example.calendarbyourselvesdacs3.presentation.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calendarbyourselvesdacs3.data.repository.event.EventRepository
import com.example.calendarbyourselvesdacs3.data.repository.sign_in.GoogleAuthUiClient
import com.example.calendarbyourselvesdacs3.domain.model.event.Event
import com.example.calendarbyourselvesdacs3.domain.model.user.UserData
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.inject.Inject

// EventViewModel này dành cho InteractWithEventScreen
@HiltViewModel
class EventViewModel @Inject constructor(
    private val repository: EventRepository,
    private val signInRepository: GoogleAuthUiClient
) : ViewModel() {
    private val _uiState = MutableStateFlow(EventUiState())
    val uiState = _uiState.asStateFlow()

    val user: UserData? = signInRepository.getSignedInUser()

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

    fun addEvent() {
        val startDay = handleDateTime(_uiState.value.startDate, _uiState.value.startTime)
        val endDay = handleDateTime(_uiState.value.endDate, _uiState.value.endTime)

        var event = Event(
            userId = user?.userId,
            title = _uiState.value.title,
            description = _uiState.value.description,
            isCheckAllDay = _uiState.value.isCheckAllDay,
            isCheckNotification = _uiState.value.isCheckNotification,
            startDay = startDay,
            endDay = endDay,
            colorIndex = _uiState.value.colorIndex
        )

        repository.addEvent(event) { completed ->
            _uiState.update {
                it.copy(eventAddedStatus = completed)
            }
        }
    }

    fun updateEvent(eventId: String){
        val startDay = handleDateTime(_uiState.value.startDate, _uiState.value.startTime)
        val endDay = handleDateTime(_uiState.value.endDate, _uiState.value.endTime)

        val event = Event(
            documentId = eventId,
            title = _uiState.value.title,
            description = _uiState.value.description,
            isCheckAllDay = _uiState.value.isCheckAllDay,
            isCheckNotification = _uiState.value.isCheckNotification,
            startDay = startDay,
            endDay = endDay,
            colorIndex = _uiState.value.colorIndex
        )

        viewModelScope.launch {
            repository.updateEvent(event){completed ->
                _uiState.update {
                    it.copy(eventUpdatedStatus = completed)
                }
            }
        }
    }

    fun handleDateTime(date: LocalDate?, time: LocalTime?): Timestamp {
        val combineDateTime: LocalDateTime = LocalDateTime.of(date, time)

        val zoneId: ZoneId = ZoneId.of("Asia/Ho_Chi_Minh")
        val zoneDateTime: ZonedDateTime = combineDateTime.atZone(zoneId)

        val convertInstant = zoneDateTime.toInstant()

        val day: Timestamp = Timestamp(convertInstant)

        return day
    }
}