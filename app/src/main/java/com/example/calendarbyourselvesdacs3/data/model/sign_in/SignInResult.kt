package com.example.calendarbyourselvesdacs3.data.model.sign_in

import com.example.calendarbyourselvesdacs3.data.model.user.UserData

data class SignInResult (
    val data: UserData?,
    val errorMessage: String?
)

