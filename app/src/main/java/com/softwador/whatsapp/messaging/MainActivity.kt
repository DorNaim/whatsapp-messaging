package com.softwador.whatsapp.messaging

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.softwador.whatsapp.messaging.ui.main.SectionsPagerAdapter


class MainActivity : AppCompatActivity() {

    var PERMISSIONS_SERVICE = arrayOf(
        Manifest.permission.FOREGROUND_SERVICE,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.READ_CALL_LOG
    );

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        verifyForegroundServicePermissions(this);
        startService(Intent(this, CallService::class.java))
        setContentView(R.layout.activity_main)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
        val fab: FloatingActionButton = findViewById(R.id.fab)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    fun verifyForegroundServicePermissions(activity: Activity) {
        // Check if we have write permission
        val permission = ActivityCompat.checkSelfPermission(
            activity,
            Manifest.permission.FOREGROUND_SERVICE
        );

        // Check if we have write permission
        val phonestatepermission = ActivityCompat.checkSelfPermission(
            activity,
            Manifest.permission.READ_PHONE_STATE
        );
        if (permission != PackageManager.PERMISSION_GRANTED || phonestatepermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                activity,
                PERMISSIONS_SERVICE,
                0
            );
        }
    }

}