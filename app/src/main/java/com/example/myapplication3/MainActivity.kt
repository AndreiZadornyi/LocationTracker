package com.example.myapplication3

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.myapplication3.models.LogItem
import com.example.myapplication3.services.TrackService
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var statusService = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupView()
    }

    override fun onResume() {
        super.onResume()
        checkStatus()
        TrackService.initLogListener(LogListener())
    }

    override fun onPause() {
        super.onPause()
        TrackService.destLogListener()
    }

    fun checkStatus() {
        if(TrackService.checkStatusService(applicationContext,TrackService::class.java)){
            btn_start_stop?.text = "Stop"
            tv_status.text = "Enabled"
            statusService = true
        } else {
            btn_start_stop?.text = "Start"
            tv_status.text = "Disabled"
            statusService = false
        }
    }

    fun setupView() {

        btn_start_stop?.setOnClickListener {
            if(statusService){
                TrackService.stop(applicationContext)
            } else {
                startLocation()
            }

            checkStatus()
        }
        btn_log?.setOnClickListener {
            TrackService.destLogListener()
            val intent = Intent(this@MainActivity, LocationActivity::class.java)
            startActivity(intent)
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocation() {
        requestForegroundPermissions()
        if (foregroundPermissionApproved()) {
            if (checkGpsStatus(this)) {
                TrackService.start(applicationContext)
            } else {
                Toast.makeText(this, "GPS disabled", Toast.LENGTH_SHORT).show()
            }
        } else {
            return
        }
    }

    private fun foregroundPermissionApproved(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    private fun requestForegroundPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                0
            )
        }
    }

    @SuppressLint("ServiceCast")
    fun checkGpsStatus(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return true
        }
        return false
    }

    private inner class LogListener: LocationActivity.LogInterfece {
        override fun addLog(logItem: LogItem) {
            tv_latitude.text = logItem.latitude
            tv_longitude.text = logItem.longitude
            tv_date.text = logItem.time
        }

    }
}