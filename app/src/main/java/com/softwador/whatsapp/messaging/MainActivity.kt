package com.softwador.whatsapp.messaging

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.softwador.whatsapp.messaging.ui.main.PermissionUtils
import com.softwador.whatsapp.messaging.ui.main.SectionsPagerAdapter
import com.softwador.whatsapp.messaging.ui.main.ServiceUtils


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PermissionUtils.verifyServicePermissions(this)
        ServiceUtils.startServiceIfNotRunning(this, CallServiceOld::class.java)
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

    override fun onPause() {
        super.onPause()
        if (!CallServiceOld.stopService) {
            ServiceUtils.startServiceIfNotRunning(this, CallServiceOld::class.java)
        }
    }

    override fun onStop() {
        super.onStop()
        if (!CallServiceOld.stopService) {
            ServiceUtils.startServiceIfNotRunning(this, CallServiceOld::class.java)
        }
    }
}