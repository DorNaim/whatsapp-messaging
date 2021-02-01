package com.softwador.whatsapp.messaging;

import android.app.*
import android.app.Notification.EXTRA_NOTIFICATION_ID
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_MIN


class CallService : Service() {
    private val NOTIF_CHANNEL_ID = 101

    private val ACTION_STOP_SERVICE = "stopForegroundService"

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // do your jobs here
        println("onStartCommand!")
        print("intent is: " + intent)
        print("intent action is: " + intent!!.action)
        if (ACTION_STOP_SERVICE.equals(intent.action)) {
            println("onStartCommand trying to stop service")
            stopForeground(true);
            stopSelf();
        } else {
            startForeground()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForeground() {
        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel("whatsapp_messaging", "Whatsapp Messaging Service")
            } else {
                // If earlier version channel ID is not used
                // https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html#NotificationCompat.Builder(android.content.Context)
                ""
            }

        val stopServiceIntent = Intent(this, CallService::class.java)
        stopServiceIntent.action = ACTION_STOP_SERVICE
        stopServiceIntent.putExtra(EXTRA_NOTIFICATION_ID, 0)
        val pStopSelf =
            PendingIntent.getService(this, 0, stopServiceIntent, PendingIntent.FLAG_CANCEL_CURRENT)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
        val notification = notificationBuilder.setOngoing(true)
            .setSmallIcon(R.drawable.common_full_open_on_phone)
            .setPriority(PRIORITY_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .setContentTitle("Whatsapp messaging is waiting for calls")
            .addAction(
                R.attr.closeIcon, "Stop waiting for calls",
                pStopSelf
            )
            .build()

        startForeground(NOTIF_CHANNEL_ID, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String {
        val chan = NotificationChannel(
            channelId,
            channelName, NotificationManager.IMPORTANCE_NONE
        )
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        chan.description = "Whatsapp messaging is waiting for calls"
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }
}
