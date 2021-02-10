package com.softwador.whatsapp.messaging

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build


class StartBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED && getServiceState(context) == ServiceState.STARTED) {
            Intent(context, CallServiceOld::class.java).also {
                it.action = "START"
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    println("Starting the service in >=26 Mode from a BroadcastReceiver")
                    context.startForegroundService(it)
                    return
                }
                println("Starting the service in < 26 Mode from a BroadcastReceiver")
                context.startService(it)
            }
        }
    }
}