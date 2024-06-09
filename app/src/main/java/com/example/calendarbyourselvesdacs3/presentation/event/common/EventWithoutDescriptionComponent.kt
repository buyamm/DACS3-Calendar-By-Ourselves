package com.example.calendarbyourselvesdacs3.presentation.event.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calendarbyourselvesdacs3.R
import com.example.calendarbyourselvesdacs3.domain.model.event.Event
import com.example.calendarbyourselvesdacs3.domain.model.event.formatTimeToString

import com.example.calendarbyourselvesdacs3.ui.theme.AppColors
import com.example.calendarbyourselvesdacs3.ui.theme.DefaultColor
import com.example.calendarbyourselvesdacs3.ui.theme.GreenColor
import com.example.calendarbyourselvesdacs3.ui.theme.LocalAppColors
import com.example.calendarbyourselvesdacs3.ui.theme.RedColor
import com.example.calendarbyourselvesdacs3.ui.theme.YellowColor
import com.example.calendarbyourselvesdacs3.utils.ColorUtil

@Composable
fun EventWithoutDescriptionComponent(event: Event, onEventClick: (eventId: String) -> Unit) {
    val taskColor = ColorUtil.colors[event.colorIndex].colorValue
    val appColors = LocalAppColors.current

    val startTimeString = formatTimeToString(event.startDay)
    val endTimeString = formatTimeToString(event.endDay)

    var hourAndMinute = ""
    var hourFormat = ""


    splitTimeForLeftLine(startTimeString).forEachIndexed { index, s ->
        when(index){
            0 -> hourAndMinute = s
            1 -> hourFormat = s
        }
    }

    val startTimeText = if(event.checkAllDay) "All\nday" else "$hourAndMinute\n$hourFormat"


    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$startTimeText",
                fontFamily = FontFamily(Font(R.font.nunito_bold)),
                textAlign = TextAlign.Center
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .border(border = BorderStroke(3.dp, appColors.circleBoxAtHomeScreen), shape = CircleShape)
                )

                Divider(modifier = Modifier.width(6.dp), color = appColors.dividerColor)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onEventClick(event.documentId) },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(taskColor)
                            .weight(0.9f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    )
                    {
                        Text(
                            text = event.title,
                            fontFamily = FontFamily(Font(R.font.nunito_bold)),
                            modifier = Modifier.padding(
                                start = 8.dp,
                                top = 8.dp
                            ),
                            color = Color.White,
                            fontSize = 20.sp
                        )

                        Text(
                            text = "$startTimeString - $endTimeString",
                            fontFamily = FontFamily(Font(R.font.nunito_bold)),
                            modifier = Modifier.padding(
                                start = 8.dp,
                                bottom = 8.dp
                            ),
                            color = Color.White
                        )
                    }

                    Divider(
                        modifier = Modifier
                            .width(6.dp)
                            .weight(0.1f), color = appColors.dividerColor
                    )

                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}