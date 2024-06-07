package com.example.calendarbyourselvesdacs3.data

sealed class Resource<T>(val data: T? = null, val throwable: Throwable? = null) {
    class Loading<T>: Resource<T>()
    class Success<T>(data: T?): Resource<T>(data = data)
    class Error<T>(throwable: Throwable?): Resource<T>(throwable = throwable)

}