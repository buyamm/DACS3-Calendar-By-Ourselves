package com.example.calendarbyourselvesdacs3.presentation.navigation

import java.time.LocalDate

fun LocalDate.navArg() = toString()

fun String.localDateArg() = LocalDate.parse(this)
