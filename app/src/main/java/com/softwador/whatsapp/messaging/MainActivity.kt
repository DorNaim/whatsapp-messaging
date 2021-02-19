package com.softwador.whatsapp.messaging

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.softwador.whatsapp.messaging.ui.main.PermissionUtils
import com.softwador.whatsapp.messaging.ui.main.SectionsPagerAdapter
import java.util.*


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PermissionUtils.verifyServicePermissions(this)
        startService(Intent(this, CallServiceOld::class.java))
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


        //start scheduled task
        val myIntent = Intent(this, StartServiceBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, myIntent, 0)

        val calendar: Calendar = Calendar.getInstance()
        calendar.setTimeInMillis(System.currentTimeMillis())
        calendar.add(Calendar.SECOND, 60) // first time

        val frequency = (60 * 1000).toLong() // in ms

        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.getTimeInMillis(),
            frequency,
            pendingIntent
        )
    }

    override fun onPause() {
        super.onPause()
        startService(Intent(this, CallServiceOld::class.java))
    }

    override fun onStop() {
        super.onStop()
        startService(Intent(this, CallServiceOld::class.java))
    }
}