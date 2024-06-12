package com.example.calendarbyourselvesdacs3.presentation.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calendarbyourselvesdacs3.R
import com.example.calendarbyourselvesdacs3.domain.model.event.Event
import com.example.calendarbyourselvesdacs3.domain.model.event.formatTimeToString
import com.example.calendarbyourselvesdacs3.presentation.event.component.splitTimeForLeftLine
import com.example.calendarbyourselvesdacs3.ui.theme.LocalAppColors
import com.example.calendarbyourselvesdacs3.utils.ColorUtil

@Composable
fun EventComponentLikeApple(
    event: Event,
    onEventClick: (eventId: String) -> Unit
) {
    val taskColor = ColorUtil.colors[event.colorIndex].colorValue
    val appColors = LocalAppColors.current

    val startTimeString = formatTimeToString(event.startDay)

    var hourAndMinute = ""
    var hourFormat = ""


    splitTimeForLeftLine(startTimeString).forEachIndexed { index, s ->
        when (index) {
            0 -> hourAndMinute = s
            1 -> hourFormat = s
        }
    }

    val startTimeText = if (event.checkAllDay) "all-day" else "$hourAndMinute $hourFormat"


    Column(modifier = Modifier
        .fillMaxSize()
        .padding(4.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onEventClick(event.documentId) },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(30.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(taskColor)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = event.title,
                    fontFamily = FontFamily(
                        Font(R.font.nunito_bold)
                    ),
                    fontSize = 22.sp
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = startTimeText, fontSize = 16.sp,
                    fontFamily = FontFamily(
                        Font(R.font.nunito_bold)
                    ),
                    color = Color.DarkGray
                )
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewScheduleList() {
//    ScheduleList()
//}


















