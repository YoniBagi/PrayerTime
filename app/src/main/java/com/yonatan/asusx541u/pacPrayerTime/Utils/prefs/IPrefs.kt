package com.yonatan.asusx541u.pacPrayerTime.Utils.prefs

interface IPrefs {
    fun setAgreementNotification(isAgree: Boolean)
    fun getAgreementNotification(): Boolean
    fun setDontShoeNotificationDialog()
    fun getDontShoeNotificationDialog(): Boolean
}