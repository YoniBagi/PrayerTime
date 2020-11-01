package com.yonatan.asusx541u.pacPrayerTime.Utils.prefs

import android.content.Context
import android.content.SharedPreferences
import com.yonatan.asusx541u.pacPrayerTime.AppSorek

const val PREFS_FILE_NAME = "sorekNetApp"

const val KEY_AGREEMENT_NOTIFICATION = "KEY_AGREEMENT_NOTIFICATION"
const val KEY_DONT_SHOW_NOTIFICATION_DIALOG = "KEY_DONT_SHOW_NOTIFICATION_DIALOG"

class Prefs : IPrefs{
    private val ctx = AppSorek.getAppContext()
    private val prefs: SharedPreferences = ctx.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = prefs.edit()


    override fun setAgreementNotification(isAgree: Boolean) {
        editor.putBoolean(KEY_AGREEMENT_NOTIFICATION, isAgree).apply()
    }

    override fun getAgreementNotification(): Boolean = prefs.getBoolean(KEY_AGREEMENT_NOTIFICATION, true)

    override fun setDontShoeNotificationDialog() {
        editor.putBoolean(KEY_DONT_SHOW_NOTIFICATION_DIALOG, true).apply()
    }

    override fun getDontShoeNotificationDialog(): Boolean = prefs.getBoolean(KEY_DONT_SHOW_NOTIFICATION_DIALOG, false)

}