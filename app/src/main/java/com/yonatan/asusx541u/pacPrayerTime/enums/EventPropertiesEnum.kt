package com.yonatan.asusx541u.pacPrayerTime.enums

enum class EventPropertiesEnum(private val mName: String) {
    SCREEN_OPENED("screen opened"),
    NOTIFICATION_RECEIVED("notification received");

    override fun toString(): String {
        return mName
    }
}