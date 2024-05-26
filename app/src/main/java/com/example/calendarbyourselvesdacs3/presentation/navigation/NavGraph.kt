package com.example.calendarbyourselvesdacs3.presentation.navigation

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.calendarbyourselvesdacs3.data.repository.sign_in.GoogleAuthUiClient
import com.example.calendarbyourselvesdacs3.presentation.calendar.day.DayEventsScreen
import com.example.calendarbyourselvesdacs3.presentation.calendar.month.CalendarMonthScreen
import com.example.calendarbyourselvesdacs3.presentation.event.InteractWithTaskScreen
import com.example.calendarbyourselvesdacs3.presentation.event.ListEventScreen
import com.example.calendarbyourselvesdacs3.presentation.events.create.CreateEventScreen
import com.example.calendarbyourselvesdacs3.presentation.search.SearchScreen
import com.example.calendarbyourselvesdacs3.presentation.sign_in.SignInScreen
import com.example.calendarbyourselvesdacs3.presentation.sign_in.SignInViewModel
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch


@SuppressLint("StateFlowValueCalledInComposition", "ComposableDestinationInComposeScope")
@Composable
fun NavGraph(viewModel: SignInViewModel, context: Context) {
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = context,
            oneTapClient = Identity.getSignInClient(context)
        )
    }

    NavHost(
        navController = navController,
        startDestination = Screen.SignInScreen.name
    ) {
        composable(Screen.SignInScreen.name) {

            val state by viewModel.uiState.collectAsStateWithLifecycle()

            //Lưu trạng thái khi đăng nhập thành công => hiển thị luôn trang chủ
            LaunchedEffect(key1 = Unit) {
<<<<<<< HEAD
                if (googleAuthUiClient.getSignedInUser() != null) {
=======
                if(googleAuthUiClient.getSignedInUser() != null) {
>>>>>>> cad15dc82e4ce79f707dba9575ac61835e30a4ad
                    navController.navigate("calendar")
                }
            }

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
                onResult = { result ->
                    if (result.resultCode == Activity.RESULT_OK) {
                        coroutineScope.launch {
                            val signInResult = googleAuthUiClient.signInWithIntent(
                                intent = result.data ?: return@launch
                            )
                            viewModel.onSignInResult(signInResult)
                        }
                    }
                }
            )

            LaunchedEffect(key1 = state.isSignInSuccessfull) {
                if (state.isSignInSuccessfull) {
                    Toast.makeText(
                        context,
                        "Sign in successful",
                        Toast.LENGTH_LONG
                    ).show()

                    navController.navigate("calendar")
                    viewModel.resetState()
                }
            }

            SignInScreen(
                state = state,
                onSignInClick = {
                    coroutineScope.launch {
                        val signInIntentSender = googleAuthUiClient.signIn()
                        launcher.launch(
                            IntentSenderRequest.Builder(
                                signInIntentSender ?: return@launch
                            ).build()
                        )
                    }
                }
            )
        }
//        composable(Screen.HomeScreen.name) {
//            HomeScreen(
//                userData = googleAuthUiClient.getSignedInUser(),
//                onSignOut = {
//                    coroutineScope.launch {
//                        googleAuthUiClient.signOut()
//                        Toast.makeText(
//                            context,
//                            "Signed out",
//                            Toast.LENGTH_LONG
//                        ).show()
//
//                        navController.popBackStack()
//                    }
//                },
//                onSearchClick = {
//                    navController.navigate(route = Screen.SearchScreen.name)
//                }
//            )
//        }


        composable("calendar") {
            CalendarMonthScreen(
                onNavigateCreateEvent = { date ->
                    navController.navigate("events/create?date=${date.navArg()}")
                },
                onNavigateDay = { date ->
                    navController.navigate("calendar/day?date=${date.navArg()}")
                },
                userData = googleAuthUiClient.getSignedInUser(),
                onSignOut = {
                    coroutineScope.launch {
                        googleAuthUiClient.signOut()
                        Toast.makeText(
                            context,
                            "Signed out",
                            Toast.LENGTH_LONG
                        ).show()

                        navController.popBackStack()
                    }
                },
                onSearchClick = {
                    navController.navigate(route = Screen.SearchScreen.name)
                }
            )
        }

        // Hiển thị danh sách sự kiện
        composable(
            route = "calendar/day?date={date}",
            arguments = listOf(navArgument(name = "date") {}),
        ) {
            DayEventsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateCreateEvent = { date ->
                    navController.navigate("events/create?date=${date.navArg()}")
                },
            )
        }

        //tạo sự kiện
        composable(
            route = "events/create?date={date}",
            arguments = listOf(navArgument(name = "date") {}),
        ) {
            CreateEventScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
            )
        }
        composable(route = Screen.SearchScreen.name) {
            SearchScreen(
                onBackClick = { navController.popBackStack() },
                onEventClick = { navController.navigate(Screen.ListEventScreen.name) })
        }

        composable(route = Screen.InteractWithTaskScreen.name) {
            InteractWithTaskScreen(onBack = { navController.popBackStack() }, onSave = {})
        }

        composable(route = Screen.ListEventScreen.name) {
            googleAuthUiClient.getSignedInUser()
                ?.let { it1 ->
                    ListEventScreen(
                        userData = it1,
                        onEventList = { navController.navigate(Screen.InteractWithTaskScreen.name) })
                }

        }
    }
}