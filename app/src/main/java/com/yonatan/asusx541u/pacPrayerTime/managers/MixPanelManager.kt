package com.yonatan.asusx541u.pacPrayerTime.managers

import android.util.Log
import com.mixpanel.android.mpmetrics.MixpanelAPI
import com.yonatan.asusx541u.pacPrayerTime.AppSorek

class MixPanelManager private constructor() {
    companion object {
        var instance: MixPanelManager? = null
            private set
            get() {
                if (field == null) {
                    field = MixPanelManager()
                }
                return field
            }
    }

    private var MIXPANEL_TOKEN = "05d2e2bbdbdfdcb4caa3a73a996a41d3"

    private val mixPanel by lazy { MixpanelAPI.getInstance(AppSorek.getAppContext(), MIXPANEL_TOKEN) }

    fun logEvent(eventName: String, eventProperties: Map<String, Any>) {
        Log.v("Mixpanel", "~~~~~~~~~~~~~~~Mixpanel~~~~~~~~~~~~~~~")
        Log.v("Mixpanel", "EventName = $eventName")
        for (key in eventProperties.keys) {
            Log.v("Mixpanel", "     " + key + " = \"" + eventProperties[key] + "\"")
        }
        Log.v("Mixpanel", "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~")
        mixPanel.trackMap(eventName, eventProperties)
    }
}