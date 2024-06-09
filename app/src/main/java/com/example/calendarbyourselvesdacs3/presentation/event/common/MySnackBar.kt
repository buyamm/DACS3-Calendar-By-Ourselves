package com.example.calendarbyourselvesdacs3.presentation.event.common

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun mySnackBar(
    scope: CoroutineScope,
    snackBarHostState: SnackbarHostState,
    msg: String,
    actionLabel: String,
    onAction: () -> Unit
){
    scope.launch {
        snackBarHostState.currentSnackbarData?.dismiss()
        val snackbarResult = snackBarHostState.showSnackbar(
            message = msg,
            actionLabel = actionLabel,
            duration = SnackbarDuration.Short
        )

        when(snackbarResult){
            SnackbarResult.ActionPerformed -> {
                onAction()
            }

            SnackbarResult.Dismissed -> {}
        }
    }
}