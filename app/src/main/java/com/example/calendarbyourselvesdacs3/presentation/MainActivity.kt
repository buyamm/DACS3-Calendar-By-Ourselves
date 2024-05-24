package com.example.calendarbyourselvesdacs3.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.calendarbyourselvesdacs3.presentation.sign_in.SignInViewModel
import com.example.calendarbyourselvesdacs3.presentation.navigation.NavGraph
import com.example.calendarbyourselvesdacs3.ui.theme.CalendarByOurselvesDACS3Theme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
 private val viewModel by viewModels<SignInViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            //calendar
            val navController = rememberNavController()


            CalendarByOurselvesDACS3Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    //==============CALENDAR===============

//                    val viewModel = viewModel<MainViewModel>()
                    NavGraph(viewModel = viewModel, context = applicationContext)
                }
            }
        }
    }
}







