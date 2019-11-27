package com.example.repit_project

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class Notification_reciver : BroadcastReceiver() {

    private var CHANNEL_ID = 0
    private var notificationId = 1001

    override fun onReceive(context: Context?, intent: Intent?) {

        val intent = Intent(context, MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = context?.let {
            NotificationCompat.Builder(it, CHANNEL_ID.toString())
                .setSmallIcon(R.drawable.ic_school)
                .setContentIntent(pendingIntent)
                .setContentTitle("REPIT")
                .setContentText("Remember to repit")
                .setAutoCancel(true)
        }

        with(context?.let { NotificationManagerCompat.from(it) }) {

            this?.notify(notificationId, builder!!.build())
        }

    }

}