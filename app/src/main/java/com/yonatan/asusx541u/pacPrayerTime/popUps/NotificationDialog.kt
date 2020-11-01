package com.yonatan.asusx541u.pacPrayerTime.popUps

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.yonatan.asusx541u.pacPrayerTime.R
import com.yonatan.asusx541u.pacPrayerTime.Utils.prefs.Prefs
import com.yonatan.asusx541u.pacPrayerTime.presenter.AboutActivity
import kotlinx.android.synthetic.main.dialog_notification.*

class NotificationDialog : DialogFragment() {

    private val iPrefs = Prefs()

    companion object {
        private var instance: NotificationDialog? = null

        fun newInstance(): NotificationDialog {
            if (instance == null) {
                instance = NotificationDialog()
            }
            return instance as NotificationDialog
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.dialog_notification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        proceedToSettingsBtn?.setOnClickListener {
            startActivity(Intent(this.context, AboutActivity::class.java))
            dismiss()
        }
        okButton?.setOnClickListener {
            dontShowAgain?.let {
                if (it.isChecked) {
                    iPrefs.setDontShoeNotificationDialog()
                    dismiss()
                }else{
                    dismiss()
                }
            }
        }
    }
}