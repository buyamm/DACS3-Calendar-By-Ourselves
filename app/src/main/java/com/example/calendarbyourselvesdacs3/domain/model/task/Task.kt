package com.example.listeventui.data



data class Task(
    val id: Int,
    val title: String,
    val description: String? = null,
    val startTime: String,
    val endTime: String
)
