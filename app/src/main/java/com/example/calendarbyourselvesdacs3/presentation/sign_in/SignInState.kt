package com.example.calendarbyourselvesdacs3.presentation.sign_in

data class SignInState(
    val isSignInSuccessfull: Boolean = false,
    val signInError: String? = null,
    val addedUserStatus: Boolean = false
)
