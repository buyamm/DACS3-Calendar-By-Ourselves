package com.example.calendarbyourselvesdacs3.presentation.home

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.calendarbyourselvesdacs3.domain.model.user.UserData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarComponent(
    userData: UserData?,
    onSignOut: () -> Unit,
    onSearchClick: () -> Unit
) {
            TopAppBar(
                title = { Text(text = "Calendar") },
                navigationIcon = {
                    IconButton(onClick = { onSearchClick() }) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                ),
                actions = {
                    if (userData?.profilPictureUrl != null) {
                        AsyncImage(
                            model = userData.profilPictureUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                    IconButton(onClick = { onSignOut() }) {
                        Icon(imageVector = Icons.Default.ExitToApp, contentDescription = null)
                    }
                }
            )



}