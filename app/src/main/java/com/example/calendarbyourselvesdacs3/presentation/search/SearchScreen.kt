package com.example.calendarbyourselvesdacs3.presentation.search

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.calendarbyourselvesdacs3.data.api.MockApi
import com.example.calendarbyourselvesdacs3.presentation.event.EventWithDateComponent

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchScreen(
    onBackClick: () -> Unit,
    onEventClick: () -> Unit
) {
    var text by remember {
        mutableStateOf("")
    }
    var active by remember {
        mutableStateOf(true)
    }

    var onSearch by remember {
        mutableStateOf(false)
    }


    Scaffold {
        SearchBar(
            query = text,
            onQueryChange = { text = it },
            onSearch = { onSearch = true }, // hanh dong nhan nut tim kiem tren ban phim
            active = active,
            onActiveChange = { active = it },
            placeholder = {
                Text(text = "Search")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = null,
                    modifier = Modifier.clickable { onBackClick() }.padding(all = 1.dp))
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        if (text == ""){
                            onBackClick()
                        }else{
                            text = ""
                            onSearch = false
                        }
                    }.padding(all = 1.dp))
            },
        ) {
            if(onSearch){
                LazyColumn(contentPadding = PaddingValues(start = 16.dp, top = 16.dp, bottom = 16.dp)) {
                    items(MockApi.taskList){
                        EventWithDateComponent(task = it, onEventClick = onEventClick)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}