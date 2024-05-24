package com.example.calendarbyourselvesdacs3.presentation.task

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.calendarbyourselvesdacs3.data.api.MockApi
import com.example.calendarbyourselvesdacs3.domain.model.user.UserData
import com.example.listeventui.presentation.component.ProfileHeaderComponent
import com.example.listeventui.presentation.component.WelcomMessageComponent

@Composable
fun ListEventScreen(
    userData: UserData,
    onTaskList: () -> Unit
) {
    LazyColumn(contentPadding = PaddingValues(start = 16.dp, top = 16.dp, bottom = 16.dp)) {
        item {
            ProfileHeaderComponent(photoUrl = userData.profilPictureUrl.toString())
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
            WelcomMessageComponent(userName = userData.username.toString())
            Spacer(modifier = Modifier.height(30.dp))
        }

        items(MockApi.taskList){
            TaskComponent(task = it, onTaskClick = onTaskList)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}