package com.example.calendarbyourselvesdacs3.presentation.event

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.AddComment
import androidx.compose.material.icons.outlined.CoPresent
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.WatchLater
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.calendarbyourselvesdacs3.data.Resource
import com.example.calendarbyourselvesdacs3.domain.model.event.PairColor
import com.example.calendarbyourselvesdacs3.domain.model.user.User
import com.example.calendarbyourselvesdacs3.domain.model.user.UserData
import com.example.calendarbyourselvesdacs3.presentation.event.component.SelectedUserResult
import com.example.calendarbyourselvesdacs3.presentation.event.component.UserSearchResult
import com.example.calendarbyourselvesdacs3.utils.ColorUtil
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

const val DEFAULT_START_TIME = "07:00 AM"
const val DEFAULT_END_TIME = "12:00 PM"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InteractWithTaskScreen(
    onBack: () -> Unit,
    onNavigateToHomePage: () -> Unit,
    eventId: String = "",
    date: LocalDate? = null,
    userData: UserData,
    viewModel: EventViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val isEventIdNotBlank = eventId.isNotBlank()
    var isCanSave by remember {
        mutableStateOf(false)
    }
    var isInitialComposition by remember { mutableStateOf(true) }
    var isHost by remember {
        mutableStateOf(false) // moi vo false
    }


    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember {
        SnackbarHostState()
    }



    LaunchedEffect(Unit) {
        if (isEventIdNotBlank) {
            viewModel.getEvent(eventId = eventId, userData = userData)
        } else {
            viewModel.resetState()
        }
    }

//    LaunchedEffect(isInitialComposition) {
//        if (isInitialComposition) {
//            isInitialComposition = false
//        } else {
//            if (uiState.hostEmail !== emailHost) {
//                mySnackBar(
//                    scope = scope,
//                    snackBarHostState = snackbarHostState,
//                    msg = "Only the host can change this event",
//                    actionLabel = "",
//                    onAction = {
//                    }
//                )
//            }
//        }
//
//    }



    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(text = "") },
                navigationIcon = {
                    IconButton(onClick = {
                        onBack()
                        viewModel.resetState()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = null,
                            tint = Color(0xFF596FB7),
                        )
                    }
                },
                actions = {
                    Box(
                        modifier = if (isCanSave && (uiState.hostEmail == userData.email || !isEventIdNotBlank)) {
                            Modifier
                                .requiredHeight(40.dp)
                                .requiredWidth(80.dp)
                                .clickable {
                                    if (isEventIdNotBlank) {
                                        viewModel.updateEvent(eventId, userData)
                                        viewModel.resetState()
                                        onBack()
                                    } else {
                                        viewModel.addEvent(userData)
                                        viewModel.resetState()
                                        onNavigateToHomePage()
                                    }
                                    Toast
                                        .makeText(
                                            context,
                                            "The operation has been performed successfully!",
                                            Toast.LENGTH_LONG
                                        )
                                        .show()
                                }
                                .background(Color.Transparent)
                        } else {
                            Modifier
                                .requiredHeight(40.dp)
                                .requiredWidth(80.dp)
                                .background(Color.Transparent)

                        },
                        contentAlignment = Alignment.Center

                    ) {
                        Text(
                            text = "Save",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (isCanSave && (uiState.hostEmail == userData.email || !isEventIdNotBlank)) Color(
                                0xFF596FB7
                            ) else Color.LightGray
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
                EditFieldTitleComponent(
                    uiState,
                    { b: Boolean ->
                        isCanSave = b
                    },
                    focusManager,
                    viewModel
                )
                Spacer(modifier = Modifier.height(30.dp))
            }
            item {
                checkAllDayComponent(
                    uiState,
                    date,
                    { b ->
                        isCanSave = b
                    },
                    viewModel
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                descriptionComponent(uiState, focusManager, viewModel)
                Spacer(modifier = Modifier.height(24.dp))
                Divider(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .height(2.dp)
                )
            }
            item {
                Spacer(modifier = Modifier.height(24.dp))
                notifcationComponent(uiState, viewModel)
                Spacer(modifier = Modifier.height(24.dp))
                Divider(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .height(2.dp)
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                userData.email?.let { it1 -> addGuestComponent(uiState, it1, viewModel) }
                Spacer(modifier = Modifier.height(24.dp))
                Divider(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .height(2.dp)
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                pickColorComponent(uiState, viewModel)
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditFieldTitleComponent(
    uiState: EventUiState,
    checkDateValidToSave: (Boolean) -> Unit,
    focusManager: FocusManager,
    viewModel: EventViewModel
) {

    LaunchedEffect(uiState.title) {
        if (uiState.title.isBlank()) {
            checkDateValidToSave(false)
        } else {
            checkDateValidToSave(true)
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(50.dp)) // Tạo khoảng trống bên trái
        TextField(
            value = uiState.title,
            onValueChange = {
                viewModel.onTitleChange(it)
            },
            placeholder = {
                Text(text = "Add title", fontSize = 20.sp)
            },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent
            ),
            textStyle = TextStyle(fontSize = 20.sp),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
            modifier = Modifier.fillMaxWidth(1f)
        )
    }
}

@Composable

fun checkAllDayComponent(
    uiState: EventUiState,
    date: LocalDate?,
    checkDateValidToSave: (Boolean) -> Unit,
    viewModel: EventViewModel
) {

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
                checked = uiState.isCheckAllDay,
                onCheckedChange = {
                    viewModel.onCheckAllDayChange(it, DEFAULT_START_TIME, DEFAULT_END_TIME)
                },
                thumbContent = {
                    Icon(
                        imageVector = if (uiState.isCheckAllDay) {
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
    Spacer(modifier = Modifier.height(20.dp))
    dataAndTimePickerComponent(
        uiState.isCheckAllDay,
        date,
        uiState,
        checkDateValidToSave,
        viewModel
    )
    Spacer(modifier = Modifier.height(30.dp))
    Divider(
        modifier = Modifier
            .fillMaxWidth(1f)
            .height(2.dp)
    )
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun dataAndTimePickerComponent(
    isCheckAllDay: Boolean,
    date: LocalDate?,
    uiState: EventUiState,
    checkDateValidToSave: (Boolean) -> Unit,
    viewModel: EventViewModel
) {
    LaunchedEffect(date) {
        if (date != null) {
            viewModel.onStartDateChange(startDate = date)
            viewModel.onEndDateChange(endDate = date)
        }
    }


    val formattedStartDate = DateTimeFormatter
        .ofPattern("E, MMM dd yyyy")
        .format(uiState.startDate)


    val formattedEndDate = DateTimeFormatter
        .ofPattern("E, MMM dd yyyy")
        .format(uiState.endDate)


    val formattedStartTime = DateTimeFormatter.ofPattern("hh:mm a").format(uiState.startTime)


    val formattedEndTime = DateTimeFormatter.ofPattern("hh:mm a").format(uiState.endTime)

    val startDateDialogState = rememberMaterialDialogState()
    val startTimeDialogState = rememberMaterialDialogState()
    val endDateDialogState = rememberMaterialDialogState()
    val endTimeDialogState = rememberMaterialDialogState()

    val startDate = uiState.startDate
    val startTime = uiState.startTime
    val endDate = uiState.endDate
    val endTime = uiState.endTime

    var isEndDateInvalid by remember {
        mutableStateOf(false)
    }

    var isEndTimeInvalid by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(startDate, endDate, startTime, endTime) {
        isEndDateInvalid = endDate.isBefore(startDate)
        isEndTimeInvalid = endTime.isBefore(startTime)

        if (isEndDateInvalid || (isEndTimeInvalid && endDate.isEqual(startDate))) {
            checkDateValidToSave(false)
        } else {
            checkDateValidToSave(true)
        }
    }

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
                    modifier = Modifier.clickable {
                        startDateDialogState.show()
                    },
                    fontSize = 17.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = formattedStartTime,
                    modifier = if (isCheckAllDay) {
                        Modifier
                    } else {
                        Modifier.clickable { startTimeDialogState.show() }
                    },
                    fontSize = 17.sp,
                    color = if (isCheckAllDay) Color.LightGray else Color.Black
                )
            }
            Row {
                Text(
                    text = formattedEndDate,
                    modifier = Modifier.clickable {
                        endDateDialogState.show()

                    },
                    fontSize = 17.sp,
                    color = if (isEndDateInvalid) Color.Red else Color.Black
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = formattedEndTime,
                    modifier = if (isCheckAllDay) {
                        Modifier
                    } else {
                        Modifier.clickable { endTimeDialogState.show() }
                    },
                    fontSize = 17.sp,
                    color = if (isEndDateInvalid || (isEndTimeInvalid && endDate.isEqual(startDate))) Color.Red else if (isCheckAllDay) Color.LightGray else Color.Black
                )
            }
        }
        MaterialDialog(
            dialogState = startDateDialogState,
            buttons = {
                positiveButton(text = "Ok") {}
                negativeButton(text = "Cancel")
            }
        ) {
            datepicker(
                initialDate = LocalDate.now(),
                title = "Pick a date",
            ) {
                viewModel.onStartDateChange(it)
            }
        }

        MaterialDialog(
            dialogState = startTimeDialogState,
            buttons = {
                positiveButton(text = "Ok") {}
                negativeButton(text = "Cancel")
            }
        ) {
            timepicker(
                initialTime = LocalTime.NOON,
                title = "Pick a time",
            ) {
                viewModel.onStartTimeChange(it)
            }
        }

        MaterialDialog(
            dialogState = endDateDialogState,
            buttons = {
                positiveButton(text = "Ok") {
//                    Toast.makeText(
//                        context,
//                        "Clicked ok",
//                        Toast.LENGTH_LONG
//                    ).show()
                }
                negativeButton(text = "Cancel")
            }
        ) {
            datepicker(
                initialDate = LocalDate.now(),
                title = "Pick a date",
            ) {
                viewModel.onEndDateChange(it)
            }
        }

        MaterialDialog(
            dialogState = endTimeDialogState,
            buttons = {
                positiveButton(text = "Ok") {
//                    Toast.makeText(
//                        context,
//                        "Clicked ok",
//                        Toast.LENGTH_LONG
//                    ).show()
                }
                negativeButton(text = "Cancel")
            }
        ) {
            timepicker(
                initialTime = LocalTime.NOON,
                title = "Pick a time",
            ) {
                viewModel.onEndTimeChange(it)
            }
        }
    }
}

@Composable
fun notifcationComponent(uiState: EventUiState, viewModel: EventViewModel) {
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
                checked = uiState.isCheckNotification,
                onCheckedChange = {
                    viewModel.onCheckNotiChange(it)
                },
                thumbContent = {
                    Icon(
                        imageVector = if (uiState.isCheckNotification) {
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
fun pickColorComponent(uiState: EventUiState, viewModel: EventViewModel) {
//    var colorIndex by remember {
//        mutableStateOf(Color.Blue)
//    }
//    var colorName by remember {
//        mutableStateOf("Default color")
//    }


    val selectedColor by animateColorAsState(
        targetValue = ColorUtil.colors[uiState.colorIndex].colorValue
    )

    var colorName = ColorUtil.colors[uiState.colorIndex].colorName

    var showDialog by remember { mutableStateOf(false) }
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.padding(start = 15.dp, end = 15.dp)) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .background(selectedColor)
            )
        }
        Text(
            text = colorName,
            fontSize = 18.sp,
            modifier = Modifier.clickable { showDialog = true })

        if (showDialog) {
            ColorPickerDialog(
                onColorSelected = { pairColor, index ->
                    viewModel.onColorChange(index)
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
    onColorSelected: (needColor: PairColor, index: Int) -> Unit,
    onDismissRequest: () -> Unit
) {
    val colors = ColorUtil.colors
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        confirmButton = { },
        title = { Text(text = "Select a Color") },
        text = {
            Column {
                colors.forEachIndexed { index, color ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .background(color.colorValue)
                            .clickable {
                                onColorSelected(PairColor(color.colorName, color.colorValue), index)
                            }
                            .padding(4.dp)
                    )
                }
            }
        }
    )
}

@Composable
fun descriptionComponent(
    uiState: EventUiState,
    focusManager: FocusManager,
    viewModel: EventViewModel
) {

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


        Box(modifier = Modifier.fillMaxWidth()) {
            if (uiState.description.isBlank()) {
                Text(
                    text = placeholderText,
                    fontSize = 20.sp,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.CenterStart)
                )
            }
            BasicTextField(
                value = uiState.description,
                onValueChange = {
                    viewModel.onDescChange(it)
                },
                textStyle = TextStyle(fontSize = 20.sp, color = Color.Black),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                modifier = Modifier
                    .fillMaxSize(1f),
            )
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun addGuestComponent(uiState: EventUiState, emailHost: String, viewModel: EventViewModel) {
    val placeholderText = "Add guest"
    var isSheetOpen by rememberSaveable {
        mutableStateOf(false)
    }
    val sheetState = rememberModalBottomSheetState()

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.CoPresent,
            contentDescription = null,
            tint = Color(0xFF52575D),
            modifier = Modifier.width(50.dp)
        )

        Box(modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (uiState.documentId == "") {
                    isSheetOpen = true
                } else {
                    if (uiState.hostEmail == emailHost) {
                        isSheetOpen = true
                    }
                }
            }
        ) {
            if (uiState.selectedUserList.isNotEmpty()) {
                EmailBoxes(
                    users = uiState.selectedUserList,
                    emailHost = emailHost,
                    uiState = uiState,
                    viewModel = viewModel
                )
            } else {
                Text(
                    text = placeholderText,
                    fontSize = 20.sp,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.CenterStart)
                )
            }
        }
    }

    if (isSheetOpen) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { isSheetOpen = false }
        ) {
            var active by remember {
                mutableStateOf(true)
            }

            SearchBar(
                query = uiState.searchQuery,
                onQueryChange = {
                    viewModel.onQueryChange(it)
                    viewModel.updateUserListSearch(it)
                },
                onSearch = { }, // hanh dong nhan nut tim kiem tren ban phim
                active = active,
                onActiveChange = { active = it },
                placeholder = {
                    Text(text = "Add guest")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = null,
                        modifier = Modifier
                            .clickable { isSheetOpen = false }
                            .padding(all = 1.dp))
                },
                trailingIcon = {
                    Text(
                        text = "Done",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF596FB7),
                        modifier = Modifier.clickable {
                            isSheetOpen = false
                        }
                    )
                },
            ) {
                if (uiState.searchQuery.isNotEmpty()) {
                    when (uiState.userList) {
                        is Resource.Error -> {
                            uiState.userList.throwable?.message?.let { it1 ->
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
                            LazyColumn(
                                contentPadding = PaddingValues(
                                    start = 16.dp,
                                    top = 16.dp,
                                    bottom = 16.dp
                                )
                            ) {
                                items(uiState.userList.data ?: emptyList()) { user ->
                                    UserSearchResult(user = user) {
                                        viewModel.onAddSelectedUser(user)
                                    }
                                }
                            }
                        }
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            top = 16.dp,
                            bottom = 16.dp
                        )
                    ) {
                        items(uiState.selectedUserList) { user ->
                            if (user.email != emailHost) {
                                SelectedUserResult(user = user) {
                                    viewModel.onRemoveSelectedUser(it)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun EmailBoxes(
    users: List<User>,
    emailHost: String,
    uiState: EventUiState,
    viewModel: EventViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        if (uiState.hostEmail == emailHost) {
            Text(
                text = "You",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .background(Color.Gray, shape = RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                color = Color.White,
                fontSize = 16.sp
            )
            users.forEach { u ->
                if (u.email != emailHost) {
                    EmailBox(email = u.email)
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        } else {
            Text(
                text = if (uiState.hostEmail != "") uiState.hostEmail else "ban",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .background(Color.Gray, shape = RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                color = Color.White,
                fontSize = 16.sp
            )
            users.forEach { u ->
                EmailBox(email = u.email)
                Spacer(modifier = Modifier.height(4.dp))
            }
        }

    }
}

@Composable
fun EmailBox(email: String) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFF6200EE).copy(alpha = 0.1f),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(all = 8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = email,
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )
        }
    }
}











