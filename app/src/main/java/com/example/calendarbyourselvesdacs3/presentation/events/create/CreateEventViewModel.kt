package com.example.calendarbyourselvesdacs3.presentation.events.create

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.calendarbyourselvesdacs3.data.remote.FirebaseRealtime
import com.example.calendarbyourselvesdacs3.domain.model.calendar.entity.EventsOfDate
import com.example.calendarbyourselvesdacs3.presentation.navigation.localDateArg
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONArray
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject
import kotlin.reflect.typeOf

@HiltViewModel
class CreateEventViewModel @Inject constructor(
    //them sự kiện
    private val savedStateHandle: SavedStateHandle,
) : ViewModel(), ContainerHost<CreateEventViewModel.State, CreateEventViewModel.SideEffect> {

    val myRef = FirebaseRealtime().myRef

    override val container = container<State, SideEffect>(
        initialState = State(),
        onCreate = {
            val initialDate = savedStateHandle.get<String>("date")?.localDateArg()
            if (initialDate != null) {
                reduce {
                    state.copy(
                        date = initialDate,
                        time = LocalTime.MIDNIGHT,
                    )
                }
            }
        },
    )

    fun onUpdateDate(date: LocalDate) {
        intent {
            val time = state.time ?: LocalTime.MIDNIGHT
            reduce {
                state.copy(
                    date = date,
                    time = time,
                )
            }
        }
    }

    fun onUpdateTime(time: LocalTime) {
        intent {
            reduce {
                state.copy(time = time)
            }
        }
    }

    //tạo sự kiện
    fun onAddEvent(
        title: String,
        description: String,
    ) {
        intent {
            val date = state.date ?: return@intent
            val time = state.time ?: return@intent

            //
            var eventsOfDate = EventsOfDate(title = title, date = date.toString())
            myRef.child(date.toString()).setValue(eventsOfDate).addOnCompleteListener{
                Log.d("thành công", "ôkkkkk")
            }.addOnFailureListener {
                Log.d("ko ổn", "ajsdkfjkajsdkl;f")
            }



            postSideEffect(SideEffect.NavigateBack)
        }
    }

    data class State(
        val date: LocalDate? = null,
        val time: LocalTime? = null,
    )

    sealed interface SideEffect {
        data object NavigateBack : SideEffect
    }
}
