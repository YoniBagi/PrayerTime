package com.yonatan.asusx541u.pacPrayerTime.managers

import com.yonatan.asusx541u.pacPrayerTime.BuildConfig
import com.yonatan.asusx541u.pacPrayerTime.enums.EventPropertiesEnum

object AnalyticsManager {

    fun logScreenOpen(nameScreen: String){
        val props = HashMap<String, String>()
        props[EventPropertiesEnum.SCREEN_OPENED.toString()] = nameScreen
        if (!BuildConfig.DEBUG){
            MixPanelManager.instance?.logEvent(EventPropertiesEnum.SCREEN_OPENED.toString(), props)
        }
    }

    fun onNotificationReceived() {
        val props = HashMap<String, String>()
        props[EventPropertiesEnum.NOTIFICATION_RECEIVED.toString()] = "notification received"
        if (!BuildConfig.DEBUG){
            MixPanelManager.instance?.logEvent(EventPropertiesEnum.NOTIFICATION_RECEIVED.toString(), props)
        }
    }
}

