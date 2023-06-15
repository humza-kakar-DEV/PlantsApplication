package com.example.plantsservicefyp.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.widget.RemoteViews
import androidx.core.content.ContextCompat.getSystemService
import com.example.plantsservicefyp.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class CustomNotification @Inject constructor(
    @ApplicationContext var context: Context,
) {

    private lateinit var notificationManager: NotificationManager
    private lateinit var notificationChannel: NotificationChannel
    private lateinit var builder: Notification.Builder
    private val channelId = "i.apps.notifications"
    private val description = "Test notification"

    fun notification(toEmail: String) {

        notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // pendingIntent is an intent for future use i.e after
        // the notification is clicked, this intent will come into action
//        val intent = Intent(this, afterNotification::class.java)

        // FLAG_UPDATE_CURRENT specifies that if a previous
        // PendingIntent already exists, then the current one
        // will update it with the latest intent
        // 0 is the request code, using it later with the
        // same method again will get back the same pending
        // intent for future reference
        // intent passed here is to our afterNotification class
//        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        // RemoteViews are used to use the content of
        // some different layout apart from the current activity layout

        // checking if android version is greater than oreo(API 26) or not

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel =
                NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_MAX)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(context, channelId)
//                .setContent(contentView)
                .setSmallIcon(R.drawable.flower_icon)
                .setContentTitle("Thank you for placing order!")
                .setContentText("Check your email ${toEmail}")
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        context.resources,
                        R.drawable.flower_icon
                    )
                )
//                .setContentIntent(pendingIntent)

        } else {

            builder = Notification.Builder(context)
//                .setContent(contentView)
                .setSmallIcon(R.drawable.flower_icon)
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        context.resources,
                        R.drawable.flower_icon
                    )
                )
//                .setContentIntent(pendingIntent)
        }

        notificationManager.notify(112, builder.build())

    }

}