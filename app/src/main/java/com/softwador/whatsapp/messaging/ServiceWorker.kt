package com.softwador.whatsapp.messaging

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.work.Worker
import androidx.work.WorkerParameters

class ServiceWorker(ctx: Context, params: WorkerParameters) : Worker(ctx, params) {

    override fun doWork(): Result {
        val serviceIntent = Intent(applicationContext, CallServiceOld::class.java)
        serviceIntent.action = "START"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            println("Starting the service in >=26 Mode from a BroadcastReceiver")
            applicationContext.startForegroundService(serviceIntent)
        }
        println("Starting the service in < 26 Mode from a BroadcastReceiver")
        applicationContext.startService(serviceIntent)
        return Result.success()
    }
}