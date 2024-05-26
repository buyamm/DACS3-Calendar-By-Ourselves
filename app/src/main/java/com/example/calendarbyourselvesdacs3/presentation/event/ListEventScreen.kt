package com.example.calendarbyourselvesdacs3.presentation.event

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.calendarbyourselvesdacs3.data.api.MockApi
import com.example.calendarbyourselvesdacs3.domain.model.user.UserData
import com.example.listeventui.data.Task
import com.example.listeventui.presentation.component.ProfileHeaderComponent
import com.example.listeventui.presentation.component.WelcomMessageComponent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListEventScreen(
    userData: UserData,
    onEventList: () -> Unit
) {
    val data = remember {
        mutableStateListOf<Task>()
    }
    LaunchedEffect(Unit) {
        data.clear()
        data.addAll(MockApi.taskList)
    }

    var openDialog by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()
    LazyColumn(contentPadding = PaddingValues(start = 16.dp, top = 16.dp, bottom = 16.dp)) {
        item {
            ProfileHeaderComponent(photoUrl = userData.profilPictureUrl.toString())
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
            WelcomMessageComponent(userName = userData.username.toString())
            Spacer(modifier = Modifier.height(30.dp))
        }

        items(data) {
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
                            if (dismissState.targetValue == DismissValue.DismissedToStart){
                                IconButton(onClick = { openDialog = true }) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        modifier = Modifier.size(30.dp)
                                    )
                                }
                                if(openDialog){
                                    AlertDialog(
                                        icon = { Icon(imageVector = Icons.Default.Delete, contentDescription = null) },
                                        title = { Text(text = "Delete Event?") },
                                        text = { Text(text = "If you click \"Confirm\" event will be deleted permanent! Be sure about it") },
                                        onDismissRequest = {
                                            openDialog = false
                                            scope.launch { dismissState.reset() }
                                        },
                                        confirmButton = {
                                            TextButton(
                                                onClick = {
                                                    data.remove(it)
                                                    scope.launch { dismissState.reset() }
                                                    openDialog = false
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
                    EventComponent(task = it, onEventClick = onEventList)
                }
            )
        }
    }
}