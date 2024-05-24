package com.example.calendarbyourselvesdacs3.domain.model.sign_in

data class SignInState(
    val isSignInSuccessfull: Boolean = false,
    val signInError: String? = null
)
