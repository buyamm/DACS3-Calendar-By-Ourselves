package com.example.calendarbyourselvesdacs3.utils

import com.example.calendarbyourselvesdacs3.domain.model.event.PairColor
import com.example.calendarbyourselvesdacs3.ui.theme.DefaultColor
import com.example.calendarbyourselvesdacs3.ui.theme.GreenColor
import com.example.calendarbyourselvesdacs3.ui.theme.RedColor
import com.example.calendarbyourselvesdacs3.ui.theme.YellowColor

object ColorUtil {
    val colors = listOf(
        PairColor("Red color", RedColor),
        PairColor("Yellow color", YellowColor),
        PairColor("Default color", DefaultColor),
        PairColor("Green color", GreenColor)
    )
}