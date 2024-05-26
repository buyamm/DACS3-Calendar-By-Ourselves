package com.example.calendarbyourselvesdacs3.presentation.home

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.calendarbyourselvesdacs3.domain.model.user.UserData

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    userData: UserData?,
    onSignOut: () -> Unit,
    onSearchClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopBarComponent(userData = userData, onSignOut = { onSignOut() }) {
                onSearchClick()
            }
        }
    ) {
        Text(text = "Calendar here!")
    }
}