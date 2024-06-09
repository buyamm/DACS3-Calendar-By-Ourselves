package com.example.listeventui.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calendarbyourselvesdacs3.R

@Composable
fun WelcomeMessageComponent(
    userName: String,
    eventQuantity: Int,
    dayOfWeek: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = "Hi $userName!", fontFamily = FontFamily(Font(R.font.nunito_extrabold)), fontSize = 22.sp)

        Text(text = "$eventQuantity task for $dayOfWeek", fontFamily = FontFamily(Font(R.font.nunito_regular)), fontSize = 18.sp, color = Color.Black)
    }
}