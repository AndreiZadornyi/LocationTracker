package com.example.myapplication3.services

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.content.ContextCompat
import android.app.ActivityManager
import android.location.Location
import android.os.Looper
import androidx.core.content.ContextCompat.getSystemService
import com.example.myapplication3.adapters.Item
import com.example.myapplication3.models.LogItem
import com.example.myapplication3.utils.Preferences
import com.google.android.gms.location.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class TrackService : Service() {
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var currentLocation: Location? = null
    private val format = SimpleDateFormat("HH:mm:ss dd-MM-yyyy", Locale.US)
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @SuppressLint("MissingPermission")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        objectCallback()

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        )

        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(applicationContext)
        locationRequest = LocationRequest().apply {
            interval = TimeUnit.SECONDS.toMillis(10)
            fastestInterval = TimeUnit.SECONDS.toMillis(10)
            maxWaitTime = TimeUnit.SECONDS.toMillis(2)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        stopSelf()
    }

    fun objectCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult?) {
                super.onLocationResult(p0)
                if (p0?.lastLocation != null) {
                    currentLocation = p0.lastLocation
                    addItem(
                        getLatitude(currentLocation!!),
                        getLongitude(currentLocation!!)
                    )
                }
            }
        }
    }

    fun getLatitude(location: Location): String = location.latitude.toDouble().toString() +"°"

    fun getLongitude(location: Location): String = location.longitude.toDouble().toString() +"°"

    fun addItem(latitude: String, longitude: String) {
//        val item = Item()
        val item = LogItem()

        item.latitude = latitude
        item.longitude = longitude
        item.time = format.format(Date())

        _context?.let { Preferences(it).addItem(item) }

//        items.add(item)
//
//        if (adapter != null) {
//            adapter!!.setItems(items)
//        }

    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        var _context:Context? = null

        fun start(context: Context){
            _context = context
            val intent = Intent(context,TrackService::class.java)
            context.startService(intent)
        }

        fun stop(context: Context){
            val intent = Intent(context,TrackService::class.java)
            context.stopService(intent)
            _context = null
        }

        fun checkStatusService(context: Context, serviceClass: Class<TrackService>) :Boolean {
            val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            for (service in manager.getRunningServices(Int.MAX_VALUE)) {
                if (serviceClass.name.equals(service.service.className)) {
                    return true
                }
            }
            return false
        }
    }
}