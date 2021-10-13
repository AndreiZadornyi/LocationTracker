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
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_location.*
import java.util.concurrent.TimeUnit

class LocationActivity : AppCompatActivity() {
    private var adapter: ItemListAdapter? = null
    private var items = ArrayList<Item>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)
        startLocation()
    }

    private fun setupView() {
        adapter = ItemListAdapter()
        rv_location.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_location.adapter = adapter

        val item_1 = Item()
        val item_2 = Item()

        item_1.latitude = "123"
        item_1.longitude = "345"

        item_2.latitude = "123"
        item_2.longitude = "345"

        items.add(item_1)
        items.add(item_2)
        if (adapter != null) {
            adapter!!.setItems(items)
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocation() {
        requestForegroundPermissions()
        if (foregroundPermissionApproved()) {
            if (checkGpsStatus(this)) {
                setupView()
                //startService(Intent(applicationContext, LocationServices::class.java))
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

}