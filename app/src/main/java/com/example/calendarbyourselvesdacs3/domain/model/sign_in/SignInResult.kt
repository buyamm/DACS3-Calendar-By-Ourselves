package com.example.calendarbyourselvesdacs3.domain.model.sign_in

import com.example.calendarbyourselvesdacs3.domain.model.user.UserData

data class SignInResult (
    val data: UserData?,
    val errorMessage: String?
)

