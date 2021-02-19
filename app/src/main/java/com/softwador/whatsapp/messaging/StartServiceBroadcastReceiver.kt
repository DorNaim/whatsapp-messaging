package com.softwador.whatsapp.messaging

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build


class StartServiceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val serviceIntent = Intent(context, CallServiceOld::class.java)
        serviceIntent.action = "START"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            println("Starting the service in >=26 Mode from a BroadcastReceiver")
            context.startForegroundService(serviceIntent)
            return
        }
        println("Starting the service in < 26 Mode from a BroadcastReceiver")
        context.startService(serviceIntent)

    }
}