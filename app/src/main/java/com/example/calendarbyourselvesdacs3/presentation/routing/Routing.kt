package com.example.calendarbyourselvesdacs3.presentation.routing

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.calendarbyourselvesdacs3.presentation.calendar.day.DayEventsScreen
import com.example.calendarbyourselvesdacs3.presentation.calendar.month.CalendarMonthScreen
import com.example.calendarbyourselvesdacs3.presentation.events.create.CreateEventScreen

@Composable
fun Routing(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "/calendar",
    ) {
        composable("/calendar") {
            CalendarMonthScreen(
                onNavigateCreateEvent = { date ->
                    navController.navigate("/events/create?date=${date.navArg()}")
                },
                onNavigateDay = { date ->
                    navController.navigate("/calendar/day?date=${date.navArg()}")
                },
//                onNavigateExport = {
//                    navController.navigate("/export")
//                },
//                onNavigateImport = {
//                    navController.navigate("/import")
//                },

            )
        }

        // Hiển thị danh sách sự kiện
        composable(
            route = "/calendar/day?date={date}",
            arguments = listOf(navArgument(name = "date") {}),
        ) {
            DayEventsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateCreateEvent = { date ->
                    navController.navigate("/events/create?date=${date.navArg()}")
                },
            )
        }

        //tạo sự kiện
        composable(
            route = "/events/create?date={date}",
            arguments = listOf(navArgument(name = "date") {}),
        ) {
            CreateEventScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
            )
        }
//        dialog("/import") {
//            ImportCalendarDialog {
//                navController.popBackStack()
//            }
//        }
//        dialog("/export") {
//            ExportCalendarDialog {
//                navController.popBackStack()
//            }
//        }
    }
}