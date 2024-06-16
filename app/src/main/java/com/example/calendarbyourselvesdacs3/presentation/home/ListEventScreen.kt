package com.example.calendarbyourselvesdacs3.presentation.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.calendarbyourselvesdacs3.data.Resource
import com.example.calendarbyourselvesdacs3.domain.model.event.getDayOfWeek
import com.example.calendarbyourselvesdacs3.domain.model.user.UserData
import com.example.calendarbyourselvesdacs3.presentation.event.component.EventComponent
import com.example.calendarbyourselvesdacs3.presentation.event.component.mySnackBar
import com.example.listeventui.presentation.component.ProfileHeaderComponent
import com.example.listeventui.presentation.component.WelcomeMessageComponent
import kotlinx.coroutines.launch
import java.time.LocalDate

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListEventScreen(
    userData: UserData,
    date: LocalDate,
    onEventClick: (eventId: String) -> Unit,
    onBack: () -> Unit,
    onNavigateToInteractEvent: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    var openDialog by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()



    LaunchedEffect(Unit) {
        viewModel.loadEventsByDate(date = date, userData = userData)
    }

    when (uiState.eventList) {
        is Resource.Error -> {
            uiState.eventList.throwable?.message?.let { it1 ->
                Text(
                    text = it1,
                    color = Color.Red
                )
            }
        }

        is Resource.Loading -> {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(align = Alignment.Center)
            )
        }

        is Resource.Success -> {
            Scaffold(
                snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                floatingActionButton = {
                    FloatingActionButton(onClick = { onNavigateToInteractEvent() }) {
                        Icon(imageVector = Icons.Rounded.Add, contentDescription = null)
                    }
                }
            ) {
                LazyColumn(
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        top = 16.dp,
                        bottom = 16.dp,
                        end = 16.dp
                    )
                ) {
                    item {
                        ProfileHeaderComponent(
                            photoUrl = userData.profilPictureUrl.toString(),
                            onBack = onBack
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(30.dp))
                        WelcomeMessageComponent(
                            userName = userData.username.toString(),
                            eventQuantity = uiState.eventQuantity,
                            dayOfWeek = getDayOfWeek(date)
                        )
                        Spacer(modifier = Modifier.height(30.dp))
                    }

                    items(uiState.eventList.data ?: emptyList()) { event ->
                        val dismissState = rememberDismissState(
                            initialValue = DismissValue.Default,
                            positionalThreshold = { swipeActivationFloat -> swipeActivationFloat / 2 }
                        )

                        SwipeToDismiss(
                            state = dismissState,
                            background = {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row {
                                        IconButton(onClick = { scope.launch { dismissState.reset() } }) {
                                            Icon(
                                                Icons.Default.Refresh,
                                                contentDescription = "Refresh",
                                                modifier = Modifier.size(30.dp)
                                            )
                                        }
                                        if (dismissState.targetValue == DismissValue.DismissedToStart) {
                                            IconButton(onClick = { openDialog = true }) {
                                                Icon(
                                                    Icons.Default.Delete,
                                                    contentDescription = "Delete",
                                                    modifier = Modifier.size(30.dp)
                                                )
                                            }
                                            if (openDialog) {
                                                AlertDialog(
                                                    icon = {
                                                        Icon(
                                                            imageVector = Icons.Default.Delete,
                                                            contentDescription = null
                                                        )
                                                    },
                                                    title = { Text(text = "Delete Event?") },
                                                    text = { Text(text = "If you click \"Confirm\" event will be deleted permanent! Be sure about it") },
                                                    onDismissRequest = {
                                                        openDialog = false
                                                        scope.launch { dismissState.reset() }
                                                    },
                                                    confirmButton = {
                                                        TextButton(
                                                            onClick = {
                                                                viewModel.deleteEvent(event, userData)
                                                                scope.launch { dismissState.reset() }
                                                                openDialog = false
                                                                mySnackBar(
                                                                    scope = scope,
                                                                    snackBarHostState = snackbarHostState,
                                                                    msg = "Deleted successfully!",
                                                                    actionLabel = "UNDO",
                                                                    onAction = {
//                                                                        viewModel.undoDeletedEvent()
                                                                    }
                                                                )
                                                            }
                                                        ) {
                                                            Text("Confirm")
                                                        }
                                                    },
                                                    dismissButton = {
                                                        TextButton(
                                                            onClick = {
                                                                openDialog = false
                                                                scope.launch { dismissState.reset() }
                                                            }
                                                        ) {
                                                            Text("Dismiss")
                                                        }
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }
                            },
                            dismissContent = {
                                EventComponent(
                                    event = event,
                                    onEventClick = { eventId ->
                                        onEventClick(eventId)
                                    }
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}


