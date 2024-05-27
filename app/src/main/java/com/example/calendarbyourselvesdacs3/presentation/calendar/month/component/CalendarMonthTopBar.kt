package com.example.calendarbyourselvesdacs3.presentation.calendar.month.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Restore
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.calendarbyourselvesdacs3.R
import com.example.calendarbyourselvesdacs3.domain.model.user.UserData
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarMonthTopBar(
    date: LocalDate?,
    showReturnToDate: Boolean,
//    onExportClicked: () -> Unit,
//    onImportClicked: () -> Unit,
    onReturnToDateClicked: () -> Unit,
    userData: UserData?,
    onSignOut: () -> Unit,
    onSearchClick: () -> Unit
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    TopAppBar(
        title = {
            date?.run {
                DateTitle(date = date)
            } ?: run {
                Text(text = stringResource(id = R.string.app_name))
            }
        },
        actions = {
            IconButton(onClick = { onSearchClick() }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
            }
            if (showReturnToDate) {
                ReturnToDateButton {
                    onReturnToDateClicked()
                }
            }
            if (userData?.profilPictureUrl != null) {
                AsyncImage(
                    model = userData.profilPictureUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .clickable { expanded = true }
                    ,
                    contentScale = ContentScale.Crop
                )
            }

            if (expanded) {
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.ExitToApp,
                                contentDescription = null
                            )
                        },
                        text = { Text(text = "Log out") },
                        onClick = { onSignOut() }
                    )
                }
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
        ),
    )
}

@Composable
private fun ReturnToDateButton(onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
    ) {
        Icon(
            painter = rememberVectorPainter(image = Icons.Rounded.Restore),
            contentDescription = null,
        )
    }
}

@Composable
private fun DateTitle(date: LocalDate) {
    Row {
        val monthText = date.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
        Column {
            Text(text = monthText)
            Text(
                modifier = Modifier.alpha(.65f),
                text = date.year.toString(),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun showBg(){
    DateTitle(date = LocalDate.now())
}