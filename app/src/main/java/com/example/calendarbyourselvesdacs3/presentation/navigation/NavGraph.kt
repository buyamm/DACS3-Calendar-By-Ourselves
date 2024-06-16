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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.calendarbyourselvesdacs3.data.repository.sign_in.GoogleAuthUiClient
import com.example.calendarbyourselvesdacs3.presentation.calendar.month.CalendarMonthScreen
import com.example.calendarbyourselvesdacs3.presentation.event.EventViewModel
import com.example.calendarbyourselvesdacs3.presentation.event.InteractWithTaskScreen
import com.example.calendarbyourselvesdacs3.presentation.home.ListEventScreen
import com.example.calendarbyourselvesdacs3.presentation.search.SearchScreen
import com.example.calendarbyourselvesdacs3.presentation.sign_in.SignInScreen
import com.example.calendarbyourselvesdacs3.presentation.sign_in.SignInViewModel
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch


@SuppressLint("StateFlowValueCalledInComposition", "ComposableDestinationInComposeScope")
@Composable
fun NavGraph(
    signInViewModel: SignInViewModel,
    eventViewModel: EventViewModel,
    context: Context
) {
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

            val state by signInViewModel.uiState.collectAsStateWithLifecycle()

            //Lưu trạng thái khi đăng nhập thành công => hiển thị luôn trang chủ
            LaunchedEffect(key1 = Unit) {
                if (googleAuthUiClient.getSignedInUser() != null) {
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
                            signInViewModel.onSignInResult(signInResult)
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
                    signInViewModel.resetState()
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


//        calendar - home page
        composable("calendar") {
            CalendarMonthScreen(
                onNavigateCreateEvent = { date ->
                    navController.navigate(Screen.InteractWithTaskScreen.name + "/create?date=${date.navArg()}")
                },
                onNavigateDay = { date ->
                    navController.navigate(Screen.ListEventScreen.name + "/event-list?date=${date.navArg()}")
                },
                onNavigateToUpdateEvent = { eventId ->
                    navController.navigate(Screen.InteractWithTaskScreen.name + "/update?eventId=$eventId")
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

                        navController.navigate(route = Screen.SignInScreen.name)
                    }
                },
                onSearchClick = {
                    navController.navigate(route = Screen.SearchScreen.name)
                }
            )
        }


        composable(route = Screen.SearchScreen.name) {
            SearchScreen(
                onBackClick = { navController.popBackStack() },
                onEventClick = { eventId ->
                    navController.navigate(Screen.InteractWithTaskScreen.name + "/update?eventId=$eventId")
                }
            )
        }

//        Create event
        composable(
//            route = Screen.InteractWithTaskScreen.name,
            route = Screen.InteractWithTaskScreen.name + "/create?date={date}",
            arguments = listOf(navArgument("date") {})
        ) {
            InteractWithTaskScreen(
                onBack = { navController.popBackStack() },
                onNavigateToHomePage = { navController.navigate("calendar") },
                date = it.arguments?.getString("date")?.localDateArg(),
                viewModel = eventViewModel
            )
        }

//        Update event
        composable(
            route = Screen.InteractWithTaskScreen.name + "/update?eventId={eventId}",
//            route = Screen.InteractWithTaskScreen.name,
            arguments = listOf(navArgument("eventId") {
                type = NavType.StringType
                defaultValue = ""
            })
        ) {
            InteractWithTaskScreen(
                onBack = { navController.popBackStack() },
                onNavigateToHomePage = { navController.navigate("calendar") },
                eventId = it.arguments?.getString("eventId") as String,
                viewModel = eventViewModel
            )
        }

//        Event List
        composable(
            route = Screen.ListEventScreen.name + "/event-list?date={date}"
//            route = Screen.ListEventScreen.name
        ) {
//            val date = LocalDate.parse("2024-06-09") // kiểu LocalDate
            val date = it.arguments?.getString("date")?.localDateArg()!!
            googleAuthUiClient.getSignedInUser()
                ?.let { it1 ->
                    ListEventScreen(
                        userData = it1,
                        date = date,
                        onEventClick = { eventId ->
                            navController.navigate(Screen.InteractWithTaskScreen.name + "/update?eventId=$eventId")
                        },
                        onBack = { navController.popBackStack() },
                        onNavigateToInteractEvent = {
                            navController.navigate(route = Screen.InteractWithTaskScreen.name + "/create?date=$date")
                        }
                    )
                }
        }
    }
}