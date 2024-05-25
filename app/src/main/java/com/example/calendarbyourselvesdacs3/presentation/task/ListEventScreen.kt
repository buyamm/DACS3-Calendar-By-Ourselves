package com.example.calendarbyourselvesdacs3.presentation.task

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.DismissValue
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.calendarbyourselvesdacs3.domain.model.user.UserData
import com.example.listeventui.presentation.component.ProfileHeaderComponent
import com.example.listeventui.presentation.component.WelcomMessageComponent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListEventScreen(
    userData: UserData,
    onEventList: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val animalList = remember { mutableStateListOf("Dog", "Cat", "Bird", "Snake") }
    LazyColumn(contentPadding = PaddingValues(start = 16.dp, top = 16.dp, bottom = 16.dp)) {
        item {
            ProfileHeaderComponent(photoUrl = userData.profilPictureUrl.toString())
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
            WelcomMessageComponent(userName = userData.username.toString())
            Spacer(modifier = Modifier.height(30.dp))
        }

        items(animalList) {
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
                                Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                            }
                            if (dismissState.targetValue == DismissValue.DismissedToStart)
                                IconButton(onClick = {
                                    animalList.remove(it)
                                    scope.launch { dismissState.reset() }
                                }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                                }
                        }
                    }
                },
                dismissContent = {
//                    EventComponent(task = it, onTaskClick = onEventList)
//                    Spacer(modifier = Modifier.height(16.dp))
                    Card {
                        ListItem(
                            headlineContent = { Text(it) },
                            supportingContent = { Text("Swipe To Delete") }
                        )
                        Divider(thickness = 5.dp, color = Color.Black)
                    }
                }
            )

        }
    }
}