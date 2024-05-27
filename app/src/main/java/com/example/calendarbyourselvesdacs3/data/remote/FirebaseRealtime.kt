package com.example.calendarbyourselvesdacs3.data.remote

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FirebaseRealtime {
    //firebase realtime
    val myRef = Firebase.database.getReference("Event")
}