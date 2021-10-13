package com.example.myapplication3.models

import com.google.gson.annotations.SerializedName

class LogItem() {
    @SerializedName("latitude")
    var latitude: String = ""

    @SerializedName("longitude")
    var longitude: String = ""

    @SerializedName("time")
    var time: String = ""
}

class LogLocation() {
    @SerializedName("logs")
    var logs: ArrayList<LogItem>? = null
}