package com.yonatan.asusx541u.pacPrayerTime.managers

import com.yonatan.asusx541u.pacPrayerTime.enums.EventPropertiesEnum

object AnalyticsManager {

    fun logScreenOpen(nameScreen: String){
        val props = HashMap<String, String>()
        props[EventPropertiesEnum.SCREEN_OPENED.toString()] = nameScreen
        MixPanelManager.instance?.logEvent(EventPropertiesEnum.SCREEN_OPENED.toString(), props)
    }
}

