package com.softwador.whatsapp.messaging.ui.main

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Build
import com.softwador.whatsapp.messaging.CallServiceOld


class ServiceUtils {

    companion object {

        var REQUEST_EXTERNAL_STORAGE = 1

        fun startServiceIfNotRunning(context: Context,serviceClass: Class<*>) {

            val myServiceRunning = isMyServiceRunning(context, serviceClass)

            if (!myServiceRunning) {
                println("Service is not running - starting it")
                startService(context, serviceClass)
            }

        }

        private fun startService(context: Context, serviceClass: Class<*>) {
            val serviceIntent = Intent(context, serviceClass)
            serviceIntent.action = "START"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                println("Starting the service in >=26 Mode from a BroadcastReceiver")
                context.startForegroundService(serviceIntent)
            }
            println("Starting the service in < 26 Mode from a BroadcastReceiver")
            context.startService(serviceIntent)
        }

//        fun isServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
//            val activityManager = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
//            val services: List<ActivityManager.RunningServiceInfo> =
//                activityManager.getRunningServices(Int.MAX_VALUE)
//            for (runningServiceInfo in services) {
//                if (runningServiceInfo.service.getClassName().equals(serviceClass.name)) {
//                    return true
//                }
//            }
//            return false
//        }

        fun isMyServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
            println("Check if service is running")
            val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            return manager.getRunningServices(Integer.MAX_VALUE)
                .any { it.service.className == serviceClass.name }
        }

    }
}