package com.yonatan.asusx541u.pacPrayerTime.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.squareup.picasso.Picasso
import com.squareup.picasso.Picasso.LoadedFrom
import com.squareup.picasso.Target
import com.yonatan.asusx541u.pacPrayerTime.R
import com.yonatan.asusx541u.pacPrayerTime.Utils.prefs.Prefs
import com.yonatan.asusx541u.pacPrayerTime.managers.AnalyticsManager
import com.yonatan.asusx541u.pacPrayerTime.presenter.MainActivity
import kotlin.random.Random


const val TAG = "SorekFirebaseMessaging"
const val NOTIFICATION_CHANNEL_ID = "101"

class SorekFirebaseMessagingService : FirebaseMessagingService() {
    private lateinit var notificationManager: NotificationManager
    private lateinit var mRemoteMessage: RemoteMessage
    private val iPrefs = Prefs()

    private val target: Target = object : Target {
        override fun onBitmapLoaded(bitmap: Bitmap, from: LoadedFrom) {
            sendNotification(bitmap)
        }
        override fun onPrepareLoad(placeHolderDrawable: Drawable) {}
        override fun onBitmapFailed(errorDrawable: Drawable?) {}
    }
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "data: ${remoteMessage.data} , notification: title: ${remoteMessage.data["title"]}, body: ${remoteMessage.data["body"]}")
        AnalyticsManager.onNotificationReceived()

        if (iPrefs.getAgreementNotification()){
            mRemoteMessage = remoteMessage
            getImage()
        }
    }

    private fun getImage() {
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            Picasso.with(this).load(mRemoteMessage.data["imageUrl"]).placeholder(R.drawable.ic_launcher_round).into(target)}
    }

    private fun sendNotification(bitmap: Bitmap) {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotificationChannel()
        }

        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0)

        val notificationBuilder =
                NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentTitle(mRemoteMessage.data["title"])
                        .setAutoCancel(true)
                        .setContentText(mRemoteMessage.data["textBody"])
                        .setShowWhen(true)
                        .setContentIntent(pendingIntent)
                        .setLargeIcon(bitmap)

        if (mRemoteMessage.data["bigPicture"].equals("true")){
            val style = NotificationCompat.BigPictureStyle()
            style.bigPicture(bitmap)
            notificationBuilder.setStyle(style)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            notificationBuilder.priority = NotificationManager.IMPORTANCE_HIGH
        } else {
            notificationBuilder.priority = Notification.PRIORITY_MAX
        }
        notificationManager.notify(Random.nextInt(500 - 35 + 1) + 35, notificationBuilder.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Sorek Notification",
                NotificationManager.IMPORTANCE_HIGH)
        notificationChannel.enableLights(true)
        notificationChannel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
        notificationChannel.enableVibration(true)
        notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC

        notificationManager.createNotificationChannel(notificationChannel)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        FirebaseMessaging.getInstance().subscribeToTopic("all")
        Log.d(TAG,"NewToken $token")
    }

}