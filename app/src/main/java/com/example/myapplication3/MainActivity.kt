package com.example.myapplication3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    }

    fun checkStatus() {
        if(TrackService.checkStatusService(applicationContext,TrackService::class.java)){
            btn_start_stop?.text = "Stop"
            statusService = true
        } else {
            btn_start_stop?.text = "Start"
            statusService = false
        }
    }

    fun setupView() {

        btn_start_stop?.setOnClickListener {
            if(statusService){
                TrackService.stop(applicationContext)
            } else {
                TrackService.start(applicationContext)
            }

            checkStatus()
        }
        btn_log?.setOnClickListener {
            val intent = Intent(this@MainActivity, LocationActivity::class.java)
            startActivity(intent)
        }
    }
}