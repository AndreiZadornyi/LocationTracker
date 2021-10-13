package com.example.myapplication3.utils

import android.annotation.SuppressLint
import android.content.Context
import com.example.myapplication3.models.LogItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Preferences (private val context: Context){

    fun setLogLocation(logs: ArrayList<LogItem>?) {
        var s = ""
        if (logs != null) {
            val gson = Gson()
            s = gson.toJson(logs)
        }
        val edit = context.getSharedPreferences("preferences", 0).edit()
        edit.putString("location", s)
        edit.apply()
    }

    fun getLogLocation(): ArrayList<LogItem>? {
        val gson = Gson()
        val s = context.getSharedPreferences("preferences", 0)
            .getString("location", null)
        return if (s != null) {
            gson.fromJson(
                s,
                object : TypeToken<ArrayList<LogItem>?>() {}.type
            )
        } else null
    }

    fun addItem(logItem: LogItem): ArrayList<LogItem> {
        var logs = getLogLocation()

        if(logs == null) {
            logs = ArrayList<LogItem>()
        }
        logs.add(logItem)
        setLogLocation(logs)
        return logs
    }
}