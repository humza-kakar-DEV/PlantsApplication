package com.example.plantsservicefyp.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.plantsservicefyp.model.NotificationData
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReminderAlarmManager @Inject constructor(
    @ApplicationContext var context: Context
) {

    private val REMINDER_ALARM_MANAGER_ID = 122

    fun startReminder(
        reminderCalendar: Calendar,
        reminderAlarmManagerId: Int = this.REMINDER_ALARM_MANAGER_ID,
        notificationData: NotificationData,
    ) {

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val flag =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE else PendingIntent.FLAG_UPDATE_CURRENT

        val intent: PendingIntent =
            PendingIntent.getBroadcast(
                context,
                reminderAlarmManagerId,
                Intent(context.applicationContext, AlarmReceiver::class.java).apply {
                    putExtra(
                        AlarmReceiver.INTENT_NOTIFICATION_KEY,
                        NotificationData(notificationData.heading, notificationData.description)
                    )
                },
                flag
            )

        val calendar = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Calendar.getInstance(Locale.ENGLISH).apply {
                set(Calendar.DAY_OF_YEAR, LocalDate.now().dayOfYear)
                set(Calendar.MINUTE, LocalDateTime.now().minute)
                set(Calendar.SECOND, LocalDateTime.now().second)
            }
        } else {
            null
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setAlarmClock(
                    AlarmManager.AlarmClockInfo(reminderCalendar.timeInMillis, intent),
                    intent
                )
            }
        } else {
            alarmManager.setAlarmClock(
                AlarmManager.AlarmClockInfo(reminderCalendar.timeInMillis, intent),
                intent
            )
        }

    }

    fun stopReminder(
        reminderAlarmManagerId: Int = this.REMINDER_ALARM_MANAGER_ID
    ) {

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(
                context,
                reminderAlarmManagerId,
                intent,
                PendingIntent.FLAG_MUTABLE
            )
        }
        alarmManager.cancel(intent)

    }

}