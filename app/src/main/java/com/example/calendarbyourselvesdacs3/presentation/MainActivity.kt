package com.example.calendarbyourselvesdacs3.presentation

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.calendarbyourselvesdacs3.common.date.converToRequestCode
import com.example.calendarbyourselvesdacs3.domain.model.event.Quintuple
import com.example.calendarbyourselvesdacs3.presentation.alarm.MyAlarm
import com.example.calendarbyourselvesdacs3.presentation.event.EventViewModel
import com.example.calendarbyourselvesdacs3.presentation.home.HomeViewModel
import com.example.calendarbyourselvesdacs3.presentation.navigation.NavGraph
import com.example.calendarbyourselvesdacs3.presentation.sign_in.SignInViewModel
import com.example.calendarbyourselvesdacs3.ui.theme.CalendarByOurselvesDACS3Theme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val signInViewModel by viewModels<SignInViewModel>()
    private val eventViewModel  by viewModels<EventViewModel>()
    private val homeViewModel: HomeViewModel by viewModels()

    @SuppressLint("LogNotTimber")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

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
                    val state by signInViewModel.uiState.collectAsStateWithLifecycle()
                    LaunchedEffect(state.isSignInSuccessfull) {
//                        Log.d("=============", homeViewModel.getTimeNotification().toString())

                        while (true){
                            delay(10000)
                            Log.d("=============", homeViewModel.getTimeNotification().toString())
                            setAlarmsForEvents(homeViewModel.getTimeNotification())
                        }
                    }

                    NavGraph(signInViewModel = signInViewModel, eventViewModel = eventViewModel, context = applicationContext)

                }
            }
        }
    }


    @SuppressLint("UnspecifiedImmutableFlag")
    fun setAlarm(context: Context, requestCode: Int, time: Int, title: String, desc: String) {
        val timeSec = System.currentTimeMillis() + time
        val alarmManager = context.getSystemService(Activity.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, MyAlarm::class.java).apply {
            putExtra("Request CODE", requestCode)
            putExtra("title", title)
            putExtra("desc", desc)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            timeSec,
            pendingIntent
        )
    }

    fun cancelAlarm(context: Context, requestCode: Int) {
        val alarmManager = context.getSystemService(Activity.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, MyAlarm::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    fun setAlarmsForEvents(tmpList: List<Quintuple<String, String, String, String, Boolean>>) {

        lifecycleScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                var tmp: String = ""
                tmpList.forEach { (time, requestCode, title, desc, checkNotification) ->
                    if (checkNotification) {
                        tmp += desc + " | "
                        setAlarm(context = this@MainActivity, time = time.toInt(), requestCode = converToRequestCode(requestCode), title = title, desc = desc)
                    } else {
                        cancelAlarm(context = this@MainActivity, requestCode = converToRequestCode(requestCode))
                    }
                }
                Log.d("------>", tmp)
            }
        }


    }


}









