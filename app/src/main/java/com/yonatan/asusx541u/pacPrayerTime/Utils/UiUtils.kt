package com.yonatan.asusx541u.pacPrayerTime.Utils

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import android.view.View
import android.view.ViewGroup


object UiUtils {

    fun centerTitle(activity: AppCompatActivity) {
        val textViews: ArrayList<View> = ArrayList()
        activity.getWindow().getDecorView().findViewsWithText(textViews, activity.title, View.FIND_VIEWS_WITH_TEXT)
        if (textViews.size > 0) {
            var appCompatTextView: AppCompatTextView? = null
            if (textViews.size == 1) {
                appCompatTextView = textViews[0] as AppCompatTextView
            } else {
                for (v in textViews) {
                    if (v.parent is Toolbar) {
                        appCompatTextView = v as AppCompatTextView
                        break
                    }
                }
            }
            if (appCompatTextView != null) {
                val params = appCompatTextView.layoutParams
                params.width = ViewGroup.LayoutParams.MATCH_PARENT
                appCompatTextView.layoutParams = params
                appCompatTextView.textAlignment = View.TEXT_ALIGNMENT_CENTER
            }
        }
    }
}