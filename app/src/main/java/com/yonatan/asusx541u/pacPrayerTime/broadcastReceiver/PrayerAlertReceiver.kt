package com.yonatan.asusx541u.pacPrayerTime.broadcastReceiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.yonatan.asusx541u.pacPrayerTime.R
import com.yonatan.asusx541u.pacPrayerTime.Utils.Consts.EXTRA_PUSH_NOTIFICATION_PRAYER
import com.yonatan.asusx541u.pacPrayerTime.presenter.MainActivity




class PrayerAlertReceiver : BroadcastReceiver() {
    override fun onReceive(ctx: Context?, intent: Intent?) {
        val prayer = intent?.getStringExtra(EXTRA_PUSH_NOTIFICATION_PRAYER)
        createNotification(ctx, prayer)
        Toast.makeText(ctx, "reciver", Toast.LENGTH_LONG).show()
        Log.d("reciver", "noti")
    }

    private fun createNotification(ctx: Context?, prayer: String?) {
        val notificationManager = ctx?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationId = 1
        val channelId = "channel-01"
        val channelName = "Prayer Alert"

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(
                    channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(mChannel)
        }

        val intent = Intent(ctx, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(ctx, 890, intent, 0)

        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val mBuilder = NotificationCompat.Builder(ctx, channelId)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("התראת תפילת $prayer")
                .setContentText("התפילה מתחילה בעוד 5 דקות")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSound(alarmSound)
                .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
        notificationManager.notify(notificationId, mBuilder.build())
    }
}