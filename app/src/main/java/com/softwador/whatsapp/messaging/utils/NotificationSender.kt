package com.softwador.whatsapp.messaging.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.softwador.whatsapp.messaging.MainActivity
import com.softwador.whatsapp.messaging.R


class NotificationSender {


    companion object {
        var notificationId = 1

        // declaring variables
        lateinit var notificationManager: NotificationManager
        lateinit var notificationChannel: NotificationChannel
        lateinit var builder: NotificationCompat.Builder
        private val channelId = "i.apps.notifications"
        private val description = "Whatsapp Messaging Notification"

        @RequiresApi(Build.VERSION_CODES.R)
        fun sendFollowupNotification(context: Context?, number: String) {
            //make sequential notifications
            notificationId++

            // it is a class to notify the user of events that happen.
            // This is how you tell the user that something has happened in the
            // background.
            notificationManager =
                context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            //set up intent to start when notification is pressed
            val notificationIntentSendMessage = Intent(context, MainActivity::class.java)
            notificationIntentSendMessage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            notificationIntentSendMessage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            notificationIntentSendMessage.putExtra("numberFromNotification", number)
            notificationIntentSendMessage.putExtra("notificationId", notificationId);
            val contentIntentSendMessage = PendingIntent.getActivity(
                context, notificationId,
                notificationIntentSendMessage, PendingIntent.FLAG_CANCEL_CURRENT
            )
            //add another notificationId for the new action
            notificationId++
            //set up intent to start when notification is pressed to send businesss card
            val notificationIntentBusinessCard = Intent(context, MainActivity::class.java)
            notificationIntentBusinessCard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            notificationIntentBusinessCard.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            notificationIntentBusinessCard.putExtra("numberFromNotification", number)
            notificationIntentBusinessCard.putExtra("sendBusinessCardFromNotification", true)
            notificationIntentBusinessCard.putExtra("notificationId", notificationId);
            val contentIntentBusinessCard = PendingIntent.getActivity(
                context, notificationId,
                notificationIntentBusinessCard, PendingIntent.FLAG_CANCEL_CURRENT
            )

// checking if android version is greater than oreo(API 26) or not
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationChannel =
                    NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
                notificationChannel.enableLights(true)
                notificationChannel.lightColor = Color.GREEN
                notificationChannel.enableVibration(false)
                notificationManager.createNotificationChannel(notificationChannel)

                builder = NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(R.drawable.common_full_open_on_phone)
                    .setLargeIcon(
                        BitmapFactory.decodeResource(
                            context.resources,
                            R.drawable.common_full_open_on_phone
                        )
                    )
            } else {

                builder = NotificationCompat.Builder(context, channelId)
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
//            builder.setContentIntent(contentIntent)
            builder.setAutoCancel(true)
                .addAction(
                    0, "Send a message",
                    contentIntentSendMessage
                )
                .addAction(
                    0, "Send business card",
                    contentIntentBusinessCard
                )
            notificationManager.notify(
                notificationId, builder.build()
            )
        }
    }
}