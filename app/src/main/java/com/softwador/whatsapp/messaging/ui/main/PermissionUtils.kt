package com.softwador.whatsapp.messaging.ui.main

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

class PermissionUtils {

    companion object {
        // Storage Permissions
        var REQUEST_EXTERNAL_STORAGE = 1;
        var PERMISSIONS_STORAGE = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        );

        var PERMISSIONS_SERVICE = arrayOf(
            Manifest.permission.FOREGROUND_SERVICE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_CALL_LOG
        );

        fun verifyStoragePermissions(activity: Activity) {
            // Check if we have write permission
            val permission = ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            );

            if (permission != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
                ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
                );
            }
        }

        fun verifyServicePermissions(activity: Activity) {
            val foregroundServicePermission = ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.FOREGROUND_SERVICE
            );

            val phoneStatePermission = ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.READ_PHONE_STATE
            );

            val callLogPermission = ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.READ_CALL_LOG
            );

            if (foregroundServicePermission != PackageManager.PERMISSION_GRANTED || phoneStatePermission != PackageManager.PERMISSION_GRANTED || callLogPermission != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
                ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_SERVICE,
                    0
                );
            }
        }
    }
}