package com.example.myapplication3.utils

import android.annotation.SuppressLint
import android.content.Context
import com.example.myapplication3.models.LogItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Preferences(private val context: Context) {

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

    fun checkLogTime() {
        val format = SimpleDateFormat("HH:mm:ss dd-MM-yyyy", Locale.US)
        val currentDate = Date()

        var logs = getLogLocation()

        if (logs != null && logs.size > 0) {
            var index = 0
            while (index < logs.size) {
                val log = logs.get(index)
                val tempDate = format.parse(log.time)
                if (tempDate != null) {
                    val different = currentDate.time - tempDate.time
                    if (different >= 1000 * 60 * 60 * 24) {
                        logs.remove(log)
                    } else {
                        index++
                    }
                }
            }
            setLogLocation(logs)
        }
    }

    fun addItem(logItem: LogItem): ArrayList<LogItem> {
        var logs = getLogLocation()


        if (logs == null) {
            logs = ArrayList<LogItem>()
        }
        logs.add(logItem)
        setLogLocation(logs)
        checkLogTime()
        return logs
    }
}