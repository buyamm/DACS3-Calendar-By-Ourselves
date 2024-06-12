package com.example.calendarbyourselvesdacs3.presentation.search

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.calendarbyourselvesdacs3.data.Resource

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchScreen(
    onBackClick: () -> Unit,
    onEventClick: (eventId: String) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {

    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    var active by remember {
        mutableStateOf(true)
    }

    var onSearch by remember {
        mutableStateOf(true)
    }


    Scaffold {
        SearchBar(
            query = uiState.searchQuery,
            onQueryChange = {
               viewModel.onQueryChange(it)
               viewModel.updateEventListSearch(it)
            },
            onSearch = {  }, // hanh dong nhan nut tim kiem tren ban phim
            active = active,
            onActiveChange = { active = it },
            placeholder = {
                Text(text = "Search")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = null,
                    modifier = Modifier
                        .clickable { onBackClick() }
                        .padding(all = 1.dp))
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    modifier = Modifier
                        .clickable {
                            if (uiState.searchQuery == "") {
                                onBackClick()
                            } else {
                                viewModel.deleteQueryChange()

                            }
                        }
                        .padding(all = 1.dp))
            },
        ) {

            if(uiState.searchQuery.isNotEmpty()){
                when(uiState.eventList){
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
                        LazyColumn(contentPadding = PaddingValues(start = 16.dp, top = 16.dp, bottom = 16.dp)) {
                            items(uiState.eventList.data ?: emptyList()){
                                SearchResult(event = it) {eventId ->
                                    onEventClick(eventId)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}