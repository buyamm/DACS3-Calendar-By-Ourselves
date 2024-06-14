package com.example.calendarbyourselvesdacs3.common.date

import android.util.Log

//ngay bat dau
fun converToRequestCode(requestCode: String): Int {
    var arr = requestCode.split("-", ":", " ")
    var tmpString = ""
    arr.forEach() {
        tmpString += it
    }
    return ((tmpString.toLong() - 26102003)/12345).toInt()
}