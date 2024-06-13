package com.example.calendarbyourselvesdacs3.presentation.event

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calendarbyourselvesdacs3.data.repository.event.EventRepository
import com.example.calendarbyourselvesdacs3.data.repository.event.UserRepository
import com.example.calendarbyourselvesdacs3.domain.model.event.Event
import com.example.calendarbyourselvesdacs3.domain.model.event.stringToLocalTime
import com.example.calendarbyourselvesdacs3.domain.model.user.User
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
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
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(EventUiState())
    val uiState = _uiState.asStateFlow()


    val user: FirebaseUser? = repository.user()
    private var getUsersJob: Job? = null

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

    fun onAddSelectedUser(user: User) {
        _uiState.update { eventUiState ->
            val updatedList = eventUiState.selectedUserList + user
            eventUiState.copy(selectedUserList = updatedList, searchQuery = "")
        }
    }

    fun onRemoveSelectedUser(user: User) {
        _uiState.update { eventUiState ->
            val updatedList = eventUiState.selectedUserList - user
            eventUiState.copy(selectedUserList = updatedList)
        }
    }

    fun onQueryChange(searchQuery: String) {
        _uiState.update {
            it.copy(searchQuery = searchQuery)
        }
    }

    fun updateUserListSearch(query: String) {
        loadUserBySearch(query)
    }

    private fun loadUserBySearch(query: String = "") {
        _uiState.update {
            it.copy(searchQuery = query)
        }

        if (query.isNotEmpty()) {
            getUsersJob?.cancel()


            getUsersJob = viewModelScope.launch {
                userRepository.loadUserBySearch(
                    queryValue = query
                ).collect {
                    _uiState.update { eventUiState ->
                        eventUiState.copy(userList = it)
                    }
                }

            }
        }
    }

    fun onCheckAllDayChange(
        isCheckAllDay: Boolean,
        defaultStartTime: String,
        defaultEndTime: String
    ) {
        _uiState.update {
            it.copy(
                isCheckAllDay = isCheckAllDay,
                startTime = if (isCheckAllDay) stringToLocalTime(defaultStartTime) else LocalTime.now(),
                endTime = if (isCheckAllDay) stringToLocalTime(defaultEndTime) else LocalTime.now()
                    .plusHours(1)
            )
        }
    }

    fun onCheckNotiChange(isCheckNoti: Boolean) {
        _uiState.update {
            it.copy(isCheckNotification = isCheckNoti)
        }
    }

    fun resetState() {
        _uiState.update {
            EventUiState()
        }
    }

    fun addEvent() {
        val startDay = handleDateTimeToTimeStamp(_uiState.value.startDate, _uiState.value.startTime)
        val endDay = handleDateTimeToTimeStamp(_uiState.value.endDate, _uiState.value.endTime)

//        val listEmail = _uiState.value.selectedUserList.map { it.email } + user?.email
        val listEmail = _uiState.value.selectedUserList.map { it.email }
        val listUserId = _uiState.value.selectedUserList.map { it.uid }
        var listGuestEventId: List<String> = emptyList()

//        the guests does priority first
        listUserId.forEach { uid ->
            var newEvent = Event(
                userId = uid,
                title = _uiState.value.title,
                description = _uiState.value.description,
                checkAllDay = _uiState.value.isCheckAllDay,
                checkNotification = _uiState.value.isCheckNotification,
                startDay = startDay,
                endDay = endDay,
                colorIndex = _uiState.value.colorIndex,
                guest = listEmail.mapIndexed { index, email ->
                    mapOf(
                        "email" to email,
                        "eventId" to ""
                    )
                }
            )
            repository.addEvent(newEvent) { complete, eventId ->
                _uiState.update {
                    it.copy(eventAddedStatus = complete)
                }
                listGuestEventId + eventId
            }
        }

//        the last is host
        var guest: List<Map<String, String>> = listEmail.mapIndexed { index, email ->
            mapOf(
                "email" to email,
                "eventId" to listGuestEventId[index]
            )
        }
        var event = Event(
            userId = user?.uid,
            title = _uiState.value.title,
            description = _uiState.value.description,
            checkAllDay = _uiState.value.isCheckAllDay,
            checkNotification = _uiState.value.isCheckNotification,
            startDay = startDay,
            endDay = endDay,
            colorIndex = _uiState.value.colorIndex,
            guest = guest
        )

        repository.addEvent(event) { completed, eventId ->
            _uiState.update {
                it.copy(eventAddedStatus = completed)
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun updateEvent(eventId: String) {
        viewModelScope.launch {
            val startDay =
                handleDateTimeToTimeStamp(_uiState.value.startDate, _uiState.value.startTime)
            val endDay = handleDateTimeToTimeStamp(_uiState.value.endDate, _uiState.value.endTime)

            var oldGuest: List<Map<String, String>> = emptyList()
            val listEmail = _uiState.value.selectedUserList.map { it.email }
            var listGuestEventId: List<String> = emptyList()
            var canAddGuest = false


            repository.getEvent(eventId, {}) { event ->
                if (event != null && event.guest != null) {
                    oldGuest = event.guest
                }
                if (event?.host?.get("email") == user?.email) {
                    canAddGuest = true
                }
            }

            if (canAddGuest) {
                //             Add new guest
                listEmail.forEach { email ->
                    if (oldGuest.any { it["email"] == email }) { // it: Map<String,String>
                        val guestEventId = oldGuest.first{it["email"] == email}.get("eventId")
                        listGuestEventId + guestEventId
                    }else{
                        val newUser = userRepository.getUser(email)
                        var newEvent = Event(
                            userId = newUser?.uid,
                            title = _uiState.value.title,
                            description = _uiState.value.description,
                            checkAllDay = _uiState.value.isCheckAllDay,
                            checkNotification = _uiState.value.isCheckNotification,
                            startDay = startDay,
                            endDay = endDay,
                            colorIndex = _uiState.value.colorIndex,
                            guest = listEmail.mapIndexed { index, email ->
                                mapOf(
                                    "email" to email,
                                    "eventId" to ""
                                )
                            }
                        )
                        repository.addEvent(newEvent) { completed, eventId ->
                            _uiState.update {
                                it.copy(eventAddedStatus = completed)
                            }
                            listGuestEventId + eventId
                        }
                    }
                }


//             Delete old guest not in selectedUserList
                oldGuest.forEach { map ->
                    if (!listEmail.contains(map["email"])) {
                        map["eventId"]?.let { eventId ->
                            repository.deleteEvent(eventId) { completed ->
                                _uiState.update {
                                    it.copy(eventAddedStatus = completed)
                                }
                            }
                        }
                    }
                }
            }

//             Update host
            val event = Event(
                documentId = eventId,
                title = _uiState.value.title,
                description = _uiState.value.description,
                checkAllDay = _uiState.value.isCheckAllDay,
                checkNotification = _uiState.value.isCheckNotification,
                startDay = startDay,
                endDay = endDay,
                colorIndex = _uiState.value.colorIndex,
                guest = listEmail.mapIndexed { index, email ->
                    mapOf(
                        "email" to email,
                        "eventId" to listGuestEventId[index]
                    )
                }
            )
            repository.updateEvent(event) { completed ->
                _uiState.update {
                    it.copy(eventUpdatedStatus = completed)
                }
            }

        }

    }


    fun getEvent(eventId: String) {
        repository.getEvent(eventId = eventId, onError = {}) { event ->
            if (event != null) {
                setEditField(event)
            }
        }
    }


    fun handleDateTimeToTimeStamp(date: LocalDate?, time: LocalTime?): Timestamp {
        val combineDateTime: LocalDateTime = LocalDateTime.of(date, time)

        val zoneId: ZoneId = ZoneId.of("Asia/Ho_Chi_Minh")
        val zoneDateTime: ZonedDateTime = combineDateTime.atZone(zoneId)

        val convertInstant = zoneDateTime.toInstant()

        val day: Timestamp = Timestamp(convertInstant)

        return day
    }

    fun handleTimeStampToDateTime(
        timestamp: Timestamp,
        onDate: (LocalDate) -> Unit,
        onTime: (LocalTime) -> Unit
    ) {
        val dateTime: LocalDateTime = timestamp.toDate().toInstant()
            .atZone(ZoneId.of("Asia/Ho_Chi_Minh"))
            .toLocalDateTime()

        onDate.invoke(dateTime.toLocalDate())
        onTime.invoke(dateTime.toLocalTime())
    }

    fun setEditField(event: Event) {
        var startDate: LocalDate? = null
        var startTime: LocalTime? = null
        var endDate: LocalDate? = null
        var endTime: LocalTime? = null

        handleTimeStampToDateTime(
            event.startDay,
            onDate = { startDate = it },
            onTime = { startTime = it }
        )

        handleTimeStampToDateTime(
            event.endDay,
            onDate = { endDate = it },
            onTime = { endTime = it }
        )



        viewModelScope.launch {
            val listUser = event.guest.map { map ->
                userRepository.getUser(map["email"])
            }

            var nonNullUsers = listUser.filterNotNull()
            _uiState.update {
                it.copy(
                    documentId = event.documentId,
                    title = event.title,
                    description = event.description,
                    colorIndex = event.colorIndex,
                    isCheckAllDay = event.checkAllDay,
                    isCheckNotification = event.checkNotification,
                    startDate = startDate!!,
                    endDate = endDate!!,
                    startTime = startTime!!,
                    endTime = endTime!!,
                    selectedUserList = nonNullUsers,
                    isHost = user?.email == event.host["email"]
                )
            }
        }
    }
}