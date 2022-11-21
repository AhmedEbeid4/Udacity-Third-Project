package com.udacity


import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

private const val NOTIFICATION_ID = 0

fun NotificationManager.sendNotification(
    channelId: String,
    messageBody: String,
    applicationContext: Context,
    fileName: String,
    status: String) {

    val detailActivityIntent = Intent(applicationContext, DetailActivity::class.java)
    detailActivityIntent.putExtra("status", status)
    detailActivityIntent.putExtra("file", fileName)

    val buttonPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        detailActivityIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val builder = NotificationCompat.Builder(applicationContext, channelId)
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(messageBody)
        .setAutoCancel(true)
        .addAction(
            R.drawable.ic_assistant_black_24dp,
            applicationContext.getString(R.string.notification_button),
            buttonPendingIntent)
        .setPriority(NotificationCompat.PRIORITY_HIGH)

    notify(NOTIFICATION_ID, builder.build())
}