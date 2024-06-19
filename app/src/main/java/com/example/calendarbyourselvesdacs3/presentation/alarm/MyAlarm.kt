package com.example.calendarbyourselvesdacs3.presentation.alarm

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.calendarbyourselvesdacs3.R

class MyAlarm: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val requestCode = intent.getIntExtra("Request_CODE", -1)
        val title = intent.getStringExtra("title") ?: "No Title"
        val desc = intent.getStringExtra("desc") ?: "No Description"
        try {
            showNotification(context, title, desc)
        }catch (e: Exception) {
            Log.d("Receiver Ex", "Receiver Ex is: ${e}}")
        }
    }

}

@SuppressLint("NotificationPermission")
private fun showNotification(context: Context, title: String, desc: String) {

    val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val channelId = "message channel"
    val channelName = "message name"

    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val soundUri: Uri = Uri.parse("android.resource://${context.packageName}/${R.raw.sound_effect}")
        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH).apply {
            setSound(soundUri, null)
        }
        manager.createNotificationChannel(channel)
    }

    val builder = NotificationCompat.Builder(context, channelId)
        .setContentTitle(title)
        .setContentText(desc)
        .setSmallIcon(androidx.core.R.drawable.notification_bg_normal)

    manager.notify(1, builder.build())
}