package com.example.calendarbyourselvesdacs3.presentation.sign_in

import androidx.lifecycle.ViewModel
import com.example.calendarbyourselvesdacs3.domain.model.sign_in.SignInResult
import com.example.calendarbyourselvesdacs3.domain.model.sign_in.SignInState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SignInViewModel:ViewModel() {
    private val _uiState = MutableStateFlow(SignInState())
    val uiState = _uiState.asStateFlow()

    fun onSignInResult(result: SignInResult){
        _uiState.update {
            it.copy(
                isSignInSuccessfull = result.data != null,
                signInError = result.errorMessage
            )
        }
    }

    fun resetState(){
        _uiState.update { SignInState() }
    }


}