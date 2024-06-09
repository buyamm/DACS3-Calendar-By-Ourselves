package com.example.calendarbyourselvesdacs3.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.example.calendarbyourselvesdacs3.presentation.event.EventViewModel
import com.example.calendarbyourselvesdacs3.presentation.navigation.NavGraph
import com.example.calendarbyourselvesdacs3.presentation.sign_in.SignInViewModel
import com.example.calendarbyourselvesdacs3.ui.theme.CalendarByOurselvesDACS3Theme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val signInViewModel by viewModels<SignInViewModel>()
    private val eventViewModel  by viewModels<EventViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            //calendar
            val navController = rememberNavController()

            CalendarByOurselvesDACS3Theme {

                val isSystemInDarkMode = isSystemInDarkTheme()
                val systemController = rememberSystemUiController()

                SideEffect {
                    systemController.setSystemBarsColor(
                        color = Color.Transparent,
                        darkIcons = !isSystemInDarkMode
                    )
                }

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //==============CALENDAR===============

//                    val viewModel = viewModel<MainViewModel>()
                    NavGraph(signInViewModel = signInViewModel, eventViewModel = eventViewModel, context = applicationContext)



                }
            }
        }
    }
}







