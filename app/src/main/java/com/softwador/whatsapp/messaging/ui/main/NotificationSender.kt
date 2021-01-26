package com.softwador.whatsapp.messaging.ui.main

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import com.softwador.whatsapp.messaging.MainActivity
import com.softwador.whatsapp.messaging.R


class NotificationSender() {


    companion object {
        var lastNotificationNum = 1

        // declaring variables
        lateinit var notificationManager: NotificationManager
        lateinit var notificationChannel: NotificationChannel
        lateinit var builder: Notification.Builder
        private val channelId = "i.apps.notifications"
        private val description = "Whatsapp Messaging Notification"
        private val notificationId = 1

        @RequiresApi(Build.VERSION_CODES.R)
        fun sendFollowupNotification(context: Context?, number: String) {

            //make sequental notificaitons
            lastNotificationNum++

            // it is a class to notify the user of events that happen.
            // This is how you tell the user that something has happened in the
            // background.
            notificationManager =
                context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            //set up intent to start when notification is pressed
            val notificationIntent = Intent(context, MainActivity::class.java)
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            notificationIntent.putExtra("numberFromNotification", number)
            val contentIntent = PendingIntent.getActivity(
                context, lastNotificationNum,
                notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT
            )


// checking if android version is greater than oreo(API 26) or not
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationChannel =
                    NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
                notificationChannel.enableLights(true)
                notificationChannel.lightColor = Color.GREEN
                notificationChannel.enableVibration(false)
                notificationManager.createNotificationChannel(notificationChannel)

                builder = Notification.Builder(context, channelId)
                    .setSmallIcon(R.drawable.common_full_open_on_phone)
                    .setLargeIcon(
                        BitmapFactory.decodeResource(
                            context.resources,
                            R.drawable.common_full_open_on_phone
                        )
                    )
            } else {

                builder = Notification.Builder(context)
                    .setSmallIcon(R.drawable.common_full_open_on_phone)
                    .setLargeIcon(
                        BitmapFactory.decodeResource(
                            context.resources,
                            R.drawable.common_full_open_on_phone
                        )
                    )

            }

            builder.setContentTitle("Followup with a whatsapp message")
            builder.setContentText(number)
            builder.setContentIntent(contentIntent)
            builder.setAutoCancel(true)
            notificationManager.notify(
                notificationId, builder.build()
            )
        }
    }
}