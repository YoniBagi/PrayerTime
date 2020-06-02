package com.yonatan.asusx541u.pacPrayerTime

import android.app.Application
import android.content.Context



class AppSorek : Application() {
    companion object {

        private lateinit var context: Context
        fun getAppContext(): Context {
            return context
        }

    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}