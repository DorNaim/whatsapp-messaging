//package com.softwador.whatsapp.messaging;
//
//import android.app.*
//import android.app.Notification.EXTRA_NOTIFICATION_ID
//import android.content.Context
//import android.content.Intent
//import android.graphics.Color
//import android.os.Build
//import android.os.IBinder
//import android.os.PowerManager
//import android.widget.Toast
//import androidx.annotation.RequiresApi
//import androidx.core.app.NotificationCompat
//import androidx.core.app.NotificationCompat.PRIORITY_MIN
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.GlobalScope
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.launch
//
//
//class CallServiceNew : Service() {
//    private val NOTIF_CHANNEL_ID = 101
//    private val ACTION_STOP_SERVICE = "stopForegroundService"
//
//    private var wakeLock: PowerManager.WakeLock? = null
//    private var isServiceStarted = false
//
//    override fun onBind(p0: Intent?): IBinder? {
//        TODO("Not yet implemented")
//    }
//
//    override fun onCreate() {
//        super.onCreate()
//        println("The service has been created".toUpperCase())
//        var notification = createNotification()
//        startForeground(1, notification)
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        println("The service has been destroyed".toUpperCase())
//        Toast.makeText(this, "Service destroyed", Toast.LENGTH_SHORT).show()
//    }
//
//    private fun startService() {
//        if (isServiceStarted) return
//        println("Starting the foreground service task")
//        Toast.makeText(this, "Service starting its task", Toast.LENGTH_SHORT).show()
//        isServiceStarted = true
//        setServiceState(this, ServiceState.STARTED)
//
//        // we need this lock so our service gets not affected by Doze Mode
//        wakeLock =
//            (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
//                newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "EndlessService::lock").apply {
//                    acquire()
//                }
//            }
//
//        // we're starting a loop in a coroutine
//        GlobalScope.launch(Dispatchers.IO) {
//            while (isServiceStarted) {
//                launch(Dispatchers.IO) {
//                    pingFakeServer()
//                }
//                delay(1 * 60 * 1000)
//            }
//            println("End of the loop for the service")
//        }
//    }
//
//    private fun stopService() {
//        println("Stopping the foreground service")
//        Toast.makeText(this, "Service stopping", Toast.LENGTH_SHORT).show()
//        try {
//            wakeLock?.let {
//                if (it.isHeld) {
//                    it.release()
//                }
//            }
//            stopForeground(true)
//            stopSelf()
//        } catch (e: Exception) {
//            println("Service stopped without being started: ${e.message}")
//        }
//        isServiceStarted = false
//        setServiceState(this, ServiceState.STOPPED)
//    }
//
//    private fun pingFakeServer() {
//        println("[pingFakeServer]")
//    }
//
//    private fun createNotification(): Notification {
//        val notificationChannelId = "ENDLESS SERVICE CHANNEL"
//
//        // depending on the Android API that we're dealing with we will have
//        // to use a specific method to create the notification
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val notificationManager =
//                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager;
//            val channel = NotificationChannel(
//                notificationChannelId,
//                "Endless Service notifications channel",
//                NotificationManager.IMPORTANCE_HIGH
//            ).let {
//                it.description = "Endless Service channel"
//                it.enableLights(true)
//                it.lightColor = Color.RED
//                it.enableVibration(true)
//                it.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
//                it
//            }
//            notificationManager.createNotificationChannel(channel)
//        }
//
//        val pendingIntent: PendingIntent =
//            Intent(this, MainActivity::class.java).let { notificationIntent ->
//                PendingIntent.getActivity(this, 0, notificationIntent, 0)
//            }
//
//        val builder: Notification.Builder =
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) Notification.Builder(
//                this,
//                notificationChannelId
//            ) else Notification.Builder(this)
//
//        return builder
//            .setContentTitle("Endless Service")
//            .setContentText("This is your favorite endless service working")
//            .setContentIntent(pendingIntent)
//            .setSmallIcon(R.mipmap.ic_launcher)
//            .setTicker("Ticker text")
//            .setPriority(Notification.PRIORITY_HIGH) // for under android 26 compatibility
//            .build()
//    }
//
//    /**
//     * DOR IMPLEMENTATION STARTS HERE
//     */
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        // do your jobs here
//        println("onStartCommand!")
//        println("intent is: " + intent)
//        println("intent action is: " + intent!!.action)
//        if (ACTION_STOP_SERVICE.equals(intent.action)) {
//            println("onStartCommand trying to stop service")
//            stopForeground(true);
//            stopSelf();
//        } else {
//            startForeground()
//        }
//        return START_STICKY
////        return super.onStartCommand(intent, flags, startId)
//    }
//
//    private fun startForeground() {
//        val channelId =
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                createNotificationChannel("whatsapp_messaging", "Whatsapp Messaging Service")
//            } else {
//                // If earlier version channel ID is not used
//                // https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html#NotificationCompat.Builder(android.content.Context)
//                ""
//            }
//
//        val stopServiceIntent = Intent(this, CallServiceNew::class.java)
//        stopServiceIntent.action = ACTION_STOP_SERVICE
//        stopServiceIntent.putExtra(EXTRA_NOTIFICATION_ID, 0)
//        val pStopSelf =
//            PendingIntent.getService(this, 0, stopServiceIntent, PendingIntent.FLAG_CANCEL_CURRENT)
//
//        val notificationBuilder = NotificationCompat.Builder(this, channelId)
//        val notification = notificationBuilder.setOngoing(true)
//            .setSmallIcon(R.drawable.common_full_open_on_phone)
//            .setPriority(PRIORITY_MIN)
//            .setCategory(Notification.CATEGORY_SERVICE)
//            .setContentTitle("Whatsapp messaging is waiting for calls")
//            .addAction(
//                R.attr.closeIcon, "Stop waiting for calls",
//                pStopSelf
//            )
//            .build()
//
//        startForeground(NOTIF_CHANNEL_ID, notification)
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun createNotificationChannel(channelId: String, channelName: String): String {
//        val chan = NotificationChannel(
//            channelId,
//            channelName, NotificationManager.IMPORTANCE_NONE
//        )
//        chan.lightColor = Color.BLUE
//        chan.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
//        chan.description = "Whatsapp messaging is waiting for calls"
//        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        service.createNotificationChannel(chan)
//        return channelId
//    }
//}
