package com.example.calendarbyourselvesdacs3.presentation.calendar.day.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.calendarbyourselvesdacs3.domain.model.calendar.entity.Event

@Composable
fun EventsList(
    modifier: Modifier = Modifier,
    events: List<Event>,
    onDeleteEvent: (Event) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        items(events, key = { it.id }) { event ->
            EventItem(
                modifier = Modifier,
                event = event,
                onDeleteEvent = onDeleteEvent,
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}