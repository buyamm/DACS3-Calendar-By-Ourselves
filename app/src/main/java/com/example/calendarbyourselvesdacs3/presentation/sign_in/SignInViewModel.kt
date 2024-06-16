package com.example.calendarbyourselvesdacs3.presentation.sign_in

import androidx.lifecycle.ViewModel
import com.example.calendarbyourselvesdacs3.data.repository.event.UserRepository
import com.example.calendarbyourselvesdacs3.domain.model.sign_in.SignInResult
import com.example.calendarbyourselvesdacs3.domain.model.user.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SignInState())
    val uiState = _uiState.asStateFlow()

    fun onSignInResult(result: SignInResult) {
        if (result.data != null) {
            val user = result.data.email?.let {
                result.data.username?.let { it1 ->
                    User(
                        uid = result.data.userId,
                        email = it,
                        username = it1
                    )
                }
            }
            if (user != null) {
                repository.addUser(user){complete ->
                    _uiState.update {
                        it.copy(
                            addedUserStatus = complete
                        )
                    }
                }
            }
        }

        _uiState.update {
            it.copy(
                isSignInSuccessfull = result.data != null,
                signInError = result.errorMessage
            )
        }
    }

    fun resetState() {
        _uiState.update { SignInState() }
    }



}