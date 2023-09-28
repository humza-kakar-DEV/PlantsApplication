package com.example.plantsservicefyp.util

import com.example.plantsservicefyp.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ShapeDrawable.ShaderFactory
import android.media.RingtoneManager
import android.os.Build
import android.service.wallpaper.WallpaperService
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.plantsservicefyp.model.NotificationData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    companion object {
        const val NOTIFICATION_ID = 1
        const val CHANNEL_ID = "112"
        const val INTENT_NOTIFICATION_KEY = "intentNotificationData"
    }

    private lateinit var notificationData: NotificationData

    override fun onReceive(context: Context?, intent: Intent?) {

        intent?.apply {
            notificationData = getSerializableExtra(INTENT_NOTIFICATION_KEY) as NotificationData
        }

        val notificationManager = ContextCompat.getSystemService(
            context!!,
            NotificationManager::class.java
        ) as NotificationManager

        notificationManager.sendReminderNotification(
            context = context
        )
    }

    private fun NotificationManager.sendReminderNotification(
        context: Context,
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                Companion.CHANNEL_ID,
                "name",
                NotificationManager.IMPORTANCE_HIGH
            )

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

            notificationManager?.createNotificationChannel(notificationChannel)

            val notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentText(notificationData.heading)
                .setContentTitle(notificationData.description)
                .setChannelId(CHANNEL_ID)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setVibrate(longArrayOf(0, 200, 10, 500))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .build()

            notificationManager?.notify(1, notification)
        }
    }

}