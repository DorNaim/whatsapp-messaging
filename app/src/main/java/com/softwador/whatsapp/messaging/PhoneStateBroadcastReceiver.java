package com.softwador.whatsapp.messaging;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.softwador.whatsapp.messaging.ui.main.NotificationSender;

public class PhoneStateBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "PhoneStateReceiver";
    Context mContext;
    String incoming_number;
    private int prev_state;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v(TAG, "onReceive start");
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE); //TelephonyManager object
        CustomPhoneStateListener customPhoneListener = new CustomPhoneStateListener();
        telephony.listen(customPhoneListener, PhoneStateListener.LISTEN_CALL_STATE); //Register our listener with TelephonyManager

        Bundle bundle = intent.getExtras();
        incoming_number = bundle.getString("incoming_number");
        Log.v(TAG, "phoneNr: " + incoming_number);
        mContext = context;
        Log.v(TAG, "onReceive end");
    }

    /* Custom PhoneStateListener */
    public class CustomPhoneStateListener extends PhoneStateListener {

        private static final String TAG = "PhoneStateListener";

        @RequiresApi(api = Build.VERSION_CODES.R)
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            if (incomingNumber != null && incomingNumber.length() > 0)
                incoming_number = incomingNumber;

            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.d(TAG, "CALL_STATE_RINGING");
                    prev_state = state;
                    break;

                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.d(TAG, "CALL_STATE_OFFHOOK");
                    prev_state = state;
                    //happnes on call start
//                    if (null != incoming_number) {
//                        NotificationSender.Companion.sendNotification(mContext, incoming_number);
//                    }
                    break;

                case TelephonyManager.CALL_STATE_IDLE:

                    Log.d(TAG, "CALL_STATE_IDLE==>" + incomingNumber);

                    if ((prev_state == TelephonyManager.CALL_STATE_OFFHOOK)) {
                        prev_state = state;
                        //Answered Call which is ended
                        if (null != incoming_number) {
                            NotificationSender.Companion.sendFollowupNotification(mContext, incoming_number);
                        }
                    }
                    if ((prev_state == TelephonyManager.CALL_STATE_RINGING)) {
                        prev_state = state;
                        //Rejected or Missed call
                        if (null != incoming_number) {
                            NotificationSender.Companion.sendFollowupNotification(mContext, incoming_number);
                        }
                    }
                    break;
            }
        }
    }
}
