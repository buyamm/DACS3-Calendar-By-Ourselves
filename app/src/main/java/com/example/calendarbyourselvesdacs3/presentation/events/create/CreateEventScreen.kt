package com.example.calendarbyourselvesdacs3.presentation.events.create

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.calendarbyourselvesdacs3.R
import com.example.calendarbyourselvesdacs3.presentation.common.component.TopBar
import com.example.calendarbyourselvesdacs3.presentation.events.create.common.DateField
import com.example.calendarbyourselvesdacs3.presentation.events.create.common.DatePicker
import com.example.calendarbyourselvesdacs3.presentation.events.create.common.TimeField
import com.example.calendarbyourselvesdacs3.presentation.events.create.common.TimePicker
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun CreateEventScreen(
    onNavigateBack: () -> Unit,
    viewModel: CreateEventViewModel = hiltViewModel(),
) {
    val state by viewModel.collectAsState()
    var title by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    viewModel.collectSideEffect {
        when (it) {
            CreateEventViewModel.SideEffect.NavigateBack -> onNavigateBack()
        }
    }

    Scaffold(
        topBar = { TopBar(stringResource(id = R.string.create_event_title), onNavigateBack) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onAddEvent(
                        title = title,
                        description = description,
                    )
                },
            ) {
                Icon(
                    painter = rememberVectorPainter(image = Icons.Rounded.Done),
                    contentDescription = null,
                )
            }
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier.width(250.dp),
            ) {
                state.date?.let { date ->
                    DateField(
                        date = date,
                        onShowDatePicker = { showDatePicker = true },
                    )
                }

                state.time?.let { time ->
                    TimeField(
                        time = time,
                        onShowTimePicker = { showTimePicker = true },
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                value = title,
                onValueChange = { title = it },
                label = { Text(text = stringResource(id = R.string.create_event_label_title)) },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                ),
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                value = description,
                onValueChange = { description = it },
                label = { Text(text = stringResource(id = R.string.create_event_label_description)) },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                ),
            )
        }
    }

    if (showDatePicker) {
        DatePicker(
            initialDate = state.date,
            onDismissRequest = { showDatePicker = false },
            onUpdateDate = { date ->
                viewModel.onUpdateDate(date)
            },
        )
    }

    if (showTimePicker) {
        TimePicker(
            onDismissRequest = { showTimePicker = false },
            onUpdateTime = { viewModel.onUpdateTime(it) },
        )
    }
}