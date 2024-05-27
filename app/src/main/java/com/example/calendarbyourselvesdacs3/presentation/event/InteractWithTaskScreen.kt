package com.example.calendarbyourselvesdacs3.presentation.event

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.AddComment
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.WatchLater
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calendarbyourselvesdacs3.ui.theme.DefaultColor
import com.example.calendarbyourselvesdacs3.ui.theme.GreenColor
import com.example.calendarbyourselvesdacs3.ui.theme.RedColor
import com.example.calendarbyourselvesdacs3.ui.theme.YellowColor
import com.example.listeventui.data.PairColor
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
//@Preview
fun InteractWithTaskScreen(
    onBack: () -> Unit,
    onSave: () -> Unit
) {
    Scaffold(
//        modifier = Modifier.nestedScroll(TopAppBarDefaults.enterAlwaysScrollBehavior().nestedScrollConnection),
        topBar = {
            TopAppBar(
//                scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                title = { Text(text = "") },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = null,
                            tint = Color(0xFF596FB7),
                        )
                    }
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .requiredHeight(40.dp)
                            .requiredWidth(80.dp)
                            .clickable { onSave() }
                            .background(Color.Transparent),
                        contentAlignment = Alignment.Center

                    ) {
                        Text(
                            text = "Save",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF596FB7)
                        )
                    }
                },
                modifier = Modifier.background(Color.LightGray)
            )
        },
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it), contentPadding = PaddingValues(all = 16.dp)
        ) {
            item {
                EditFieldTitleComponent()
                Spacer(modifier = Modifier.height(30.dp))
            }
            item {
                checkAllDayComponent()
                Spacer(modifier = Modifier.height(20.dp))
            }
            item {
                dataAndTimePickerComponent()
                Spacer(modifier = Modifier.height(30.dp))
                Divider(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .height(2.dp)
                )
            }
            item {
                Spacer(modifier = Modifier.height(24.dp))
                descriptionComponent()
                Spacer(modifier = Modifier.height(24.dp))
                Divider(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .height(2.dp)
                )
            }
            item {
                Spacer(modifier = Modifier.height(24.dp))
                notifcationComponent()
                Spacer(modifier = Modifier.height(24.dp))
                Divider(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .height(2.dp)
                )
            }
            item {
                Spacer(modifier = Modifier.height(24.dp))
                pickColorComponent()
                Spacer(modifier = Modifier.height(24.dp))
            }

//            items(count = 50) {
//                androidx.compose.material3.ListItem(headlineContent = { Text(text = "Item $it") })
//            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditFieldTitleComponent() {
    var str by remember {
        mutableStateOf("")
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
//        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(50.dp)) // Tạo khoảng trống bên trái
        TextField(
            value = str,
            onValueChange = { str = it },
            placeholder = {
                Text(text = "Add title", fontSize = 20.sp)
            },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent
            ),
            textStyle = TextStyle(fontSize = 20.sp),
            modifier = Modifier.fillMaxWidth(1f)
        )
    }
}

@Composable
//@Preview
fun checkAllDayComponent() {
    var isCheckedAllDay by remember {
        mutableStateOf(false)
    }
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Outlined.WatchLater,
            contentDescription = null,
            tint = Color(0xFF52575D),
            modifier = Modifier.width(50.dp)
        )
        Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "All-day", fontSize = 18.sp)
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                checked = isCheckedAllDay,
                onCheckedChange = { isCheckedAllDay = it },
                thumbContent = {
                    Icon(
                        imageVector = if (isCheckedAllDay) {
                            Icons.Default.Check
                        } else {
                            Icons.Default.Close
                        },
                        contentDescription = null
                    )
                }
            )
        }
    }
}

@Composable
fun dataAndTimePickerComponent() {
    val context = LocalContext.current
    var pickedStartData by remember {
        mutableStateOf(LocalDate.now())
    }
    var pickedEndData by remember {
        mutableStateOf(LocalDate.now())
    }

    var pickedStartTime by remember {
        mutableStateOf(LocalTime.now())
    }
    var pickedEndTime by remember {
        mutableStateOf(LocalTime.now())
    }

    val formattedStartDate by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("E, MMM dd yyyy")
                .format(pickedStartData)
        }
    }

    val formattedEndDate by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("E, MMM dd yyyy")
                .format(pickedEndData)
        }
    }

    val formattedStartTime by remember {
        derivedStateOf {
            DateTimeFormatter.ofPattern("hh:mm a").format(pickedStartTime)
        }
    }

    val formattedEndTime by remember {
        derivedStateOf {
            DateTimeFormatter.ofPattern("hh:mm a").format(pickedEndTime)
        }
    }

    val startDateDialogState = rememberMaterialDialogState()
    val startTimeDialogState = rememberMaterialDialogState()
    val endDateDialogState = rememberMaterialDialogState()
    val endTimeDialogState = rememberMaterialDialogState()
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(50.dp))
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formattedStartDate,
                    modifier = Modifier.clickable { startDateDialogState.show() },
                    fontSize = 17.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = formattedStartTime,
                    modifier = Modifier.clickable { startTimeDialogState.show() },
                    fontSize = 17.sp
                )
            }
            Row {
                Text(
                    text = formattedEndDate,
                    modifier = Modifier.clickable { endDateDialogState.show() },
                    fontSize = 17.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = formattedEndTime,
                    modifier = Modifier.clickable { endTimeDialogState.show() },
                    fontSize = 17.sp
                )
            }
        }
        MaterialDialog(
            dialogState = startDateDialogState,
            buttons = {
                positiveButton(text = "Ok") {
                    Toast.makeText(
                        context,
                        "Clicked ok",
                        Toast.LENGTH_LONG
                    ).show()
                }
                negativeButton(text = "Cancel")
            }
        ) {
            datepicker(
                initialDate = LocalDate.now(),
                title = "Pick a date",
//                allowedDateValidator = {
//                    it.dayOfMonth % 2 == 1
//                }
            ) {
                pickedStartData = it
            }
        }

        MaterialDialog(
            dialogState = startTimeDialogState,
            buttons = {
                positiveButton(text = "Ok") {
                    Toast.makeText(
                        context,
                        "Clicked ok",
                        Toast.LENGTH_LONG
                    ).show()
                }
                negativeButton(text = "Cancel")
            }
        ) {
            timepicker(
                initialTime = LocalTime.NOON,
                title = "Pick a time",
            ) {
                pickedStartTime = it
            }
        }
        MaterialDialog(
            dialogState = endDateDialogState,
            buttons = {
                positiveButton(text = "Ok") {
                    Toast.makeText(
                        context,
                        "Clicked ok",
                        Toast.LENGTH_LONG
                    ).show()
                }
                negativeButton(text = "Cancel")
            }
        ) {
            datepicker(
                initialDate = LocalDate.now(),
                title = "Pick a date",
//                allowedDateValidator = {
//                    it.dayOfMonth % 2 == 1
//                }
            ) {
                pickedEndData = it
            }
        }

        MaterialDialog(
            dialogState = endTimeDialogState,
            buttons = {
                positiveButton(text = "Ok") {
                    Toast.makeText(
                        context,
                        "Clicked ok",
                        Toast.LENGTH_LONG
                    ).show()
                }
                negativeButton(text = "Cancel")
            }
        ) {
            timepicker(
                initialTime = LocalTime.NOON,
                title = "Pick a time",
            ) {
                pickedEndTime = it
            }
        }
    }
}

@Composable
fun notifcationComponent() {
    var isCheckedNotif by remember {
        mutableStateOf(false)
    }
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Outlined.NotificationsActive,
            contentDescription = null,
            tint = Color(0xFF52575D),
            modifier = Modifier.width(50.dp)
        )
        Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Add a notification", fontSize = 18.sp)
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                checked = isCheckedNotif,
                onCheckedChange = { isCheckedNotif = it },
                thumbContent = {
                    Icon(
                        imageVector = if (isCheckedNotif) {
                            Icons.Default.Check
                        } else {
                            Icons.Default.Close
                        },
                        contentDescription = null
                    )
                },
            )
        }
    }
}

@Composable
fun pickColorComponent() {
    var colorIndex by remember {
        mutableStateOf(Color.Blue)
    }
    var colorName by remember {
        mutableStateOf("Default color")
    }
    var showDialog by remember { mutableStateOf(false) }
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.padding(start = 15.dp, end = 15.dp)) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .background(colorIndex)
            )
        }
        Text(
            text = colorName,
            fontSize = 18.sp,
            modifier = Modifier.clickable { showDialog = true })

        if (showDialog) {
            ColorPickerDialog(
                onColorSelected = { pairColor ->
                    colorIndex = pairColor.colorValue
                    colorName = pairColor.colorName
                    showDialog = false
                },
                onDismissRequest = { showDialog = false }
            )
        }
    }
}
//đỏ , vang, blue, green
//#0xFFC51605, #0xFFFFBB16, #0xFF2192FF, #0xFF187322

@Composable
fun ColorPickerDialog(
    onColorSelected: (needColor: PairColor) -> Unit,
    onDismissRequest: () -> Unit
) {
    val colors = listOf(
        PairColor("Red color", RedColor),
        PairColor("Yellow color", YellowColor),
        PairColor("Default color", DefaultColor),
        PairColor("Green color", GreenColor)
    )
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        confirmButton = { },
        title = { Text(text = "Select a Color") },
        text = {
            Column {
                colors.forEach { color ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .background(color.colorValue)
                            .clickable {
                                onColorSelected(PairColor(color.colorName, color.colorValue))
                            }
                            .padding(4.dp)
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
//@Preview
fun descriptionComponent() {
    var description by remember {
        mutableStateOf("")
    }
    var isPlaceholderVisible by remember { mutableStateOf(true) }
    val placeholderText = "Add description content"
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.AddComment,
            contentDescription = null,
            tint = Color(0xFF52575D),
            modifier = Modifier.width(50.dp)
        )

        BasicTextField(
            value = description,
            onValueChange = {
                description = it
                isPlaceholderVisible = it.isEmpty()
            },
            textStyle = TextStyle(fontSize = 20.sp, color = Color.Black),
            modifier = Modifier
                .weight(1f)
                .padding(12.dp),
        ) {
            if (isPlaceholderVisible) {
                Text(
                    text = placeholderText,
                    color = Color.Gray
                )
            } else {
                Text(
                    text = description,
                    color = Color.Black,
                    fontSize = 20.sp
                )
            }
        }
    }
}














