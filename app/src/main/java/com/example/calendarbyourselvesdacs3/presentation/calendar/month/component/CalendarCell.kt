package com.example.calendarbyourselvesdacs3.presentation.calendar.month.component

import android.graphics.Paint.Style
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calendarbyourselvesdacs3.data.remote.FirebaseRealtime
import com.example.calendarbyourselvesdacs3.domain.model.calendar.entity.CalendarDate
import com.example.calendarbyourselvesdacs3.domain.model.calendar.entity.EventsOfDate
import com.example.calendarbyourselvesdacs3.domain.model.calendar.entity.isInMonth
import com.example.calendarbyourselvesdacs3.presentation.calendar.month.component.modifier.calendarCellPadding
import com.example.calendarbyourselvesdacs3.ui.theme.LocalAppColors
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

@Composable
fun CalendarCell(
    index: Int,
    cellSize: Dp,
    isToday: Boolean,
    date: CalendarDate,
    renderCell: @Composable BoxScope.() -> Unit = {},
    onCellClicked: (CalendarDate) -> Unit = {},
) {
    val appColors = LocalAppColors.current
    val myRef = FirebaseRealtime().myRef
    var stateColor by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .size(cellSize)
            .calendarCellPadding(index)
            .clip(RoundedCornerShape(2.dp))
            .let {
                if (isToday) {
                    it.background(appColors.currentDayBackground)
                } else if (date.isInMonth) {
                    it.background(appColors.inMonthBackground)
                } else {
                    it.background(appColors.outOfMonthBackground)
                }
            }
            .clickable { onCellClicked(date) },
    ) {
        Log.d("----------------------------->", date.date.toString())
        Text(
//            modifier = Modifier.padding(start = 3.dp), //3
            text = "${date.date.dayOfMonth}",
            color = appColors.calendarContent,
            fontSize = 11.sp, //11
        )
        if(stateColor){
            Text(text = "ok1211111", color = Color.Red)
        }
        
        
        
        
        
        
        
        
        
        //        ============ Đánh dấu sự kiện được thêm vào lịch  ============
        myRef.child(date.date.toString()).addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = snapshot.getValue(Info::class.java)
                if(value != null){
                    stateColor = true
                }else
                    stateColor = false



            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("Test------->", "Failed to read value.", error.toException())
            }

        })


    }
}

data class Info(val date: String? = null)