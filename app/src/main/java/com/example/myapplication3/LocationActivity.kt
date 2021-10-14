package com.example.myapplication3

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication3.adapters.Item
import com.example.myapplication3.adapters.ItemListAdapter
import com.example.myapplication3.models.LogItem
import com.example.myapplication3.services.TrackService
import com.example.myapplication3.utils.Preferences
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_location.*
import java.util.concurrent.TimeUnit

class LocationActivity : AppCompatActivity() {
    private var adapter: ItemListAdapter? = null
    private var items:ArrayList<LogItem>? = ArrayList<LogItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)
        setupView()
    }

    override fun onResume() {
        super.onResume()
        showLogs()
        TrackService.initLogListener(LogListener())
    }

    override fun onPause() {
        super.onPause()
        TrackService.destLogListener()
    }

    private fun setupView() {
        adapter = ItemListAdapter()
        rv_location.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_location.adapter = adapter
    }

    fun showLogs() {
        if(adapter != null) {
            items = Preferences(applicationContext).getLogLocation()
            if(items != null && items!!.size > 0) {
                adapter?.setItems(items!!)
            }
        }
    }

    private inner class LogListener: LogInterfece {
        override fun addLog(logItem: LogItem) {
            showLogs()
        }

    }

    interface LogInterfece {
        fun addLog(logItem: LogItem)
    }
}