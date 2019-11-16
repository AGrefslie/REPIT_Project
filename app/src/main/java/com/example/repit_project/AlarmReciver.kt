package com.example.repit_project

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReciver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationId = intent!!.getIntExtra("notificationId", 0)
        val message = intent.getStringExtra("todo")

        val mainIntent = Intent(context, MainActivity::class.java)
        val contentIntent = PendingIntent.getActivity(context, 0, mainIntent, 0)

        val mNotificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val builder = Notification.Builder(context)
        builder.setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("It's Time!")
            .setContentText(message)
            .setWhen(System.currentTimeMillis())
            .setAutoCancel(true)
            .setContentIntent(contentIntent)

        mNotificationManager.notify(notificationId, builder.build())

    }

}