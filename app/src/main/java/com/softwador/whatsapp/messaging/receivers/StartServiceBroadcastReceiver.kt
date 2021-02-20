package com.softwador.whatsapp.messaging.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.softwador.whatsapp.messaging.services.CallServiceOld
import com.softwador.whatsapp.messaging.utils.ServiceUtils


class StartServiceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        ServiceUtils.startServiceIfNotRunning(context, CallServiceOld::class.java)
//        context.stopService(serviceIntent)
    }
}