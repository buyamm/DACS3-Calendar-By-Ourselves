package com.example.calendarbyourselvesdacs3.presentation.event.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calendarbyourselvesdacs3.domain.model.user.User

@Composable
fun SelectedUserResult(user: User, onDeleteUser: (User) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = user.email,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 14.sp,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                modifier = Modifier.clickable { onDeleteUser(user) })

        }
    }
}