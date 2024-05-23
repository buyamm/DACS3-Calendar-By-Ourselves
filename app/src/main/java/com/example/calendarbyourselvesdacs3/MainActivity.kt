package com.example.calendarbyourselvesdacs3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.calendarbyourselvesdacs3.presentation.sign_in.SignInViewModel
import com.example.calendarbyourselvesdacs3.presentation.navigation.NavGraph
import com.example.calendarbyourselvesdacs3.ui.theme.CalendarByOurselvesDACS3Theme


class MainActivity : ComponentActivity() {
 private val viewModel by viewModels<SignInViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CalendarByOurselvesDACS3Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
//                    val viewModel = viewModel<MainViewModel>()
                    NavGraph(viewModel = viewModel, context = applicationContext)
                }
            }
        }
    }
}







