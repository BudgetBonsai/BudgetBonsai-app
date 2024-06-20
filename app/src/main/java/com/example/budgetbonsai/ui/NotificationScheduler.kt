package com.example.budgetbonsai.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log

class NotificationScheduler(private val context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("notification_prefs", Context.MODE_PRIVATE)

    fun scheduleNotification(type: String) {
        if (isNotificationScheduled(type)) return

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            type.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val intervalMillis = when (type) {
            "Daily" -> AlarmManager.INTERVAL_FIFTEEN_MINUTES
            "Weekly" -> AlarmManager.INTERVAL_DAY * 7
            "Monthly" -> AlarmManager.INTERVAL_DAY * 30
            else -> AlarmManager.INTERVAL_DAY
        }

        val triggerAtMillis = System.currentTimeMillis() + intervalMillis

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            intervalMillis,
            pendingIntent
        )

        setNotificationScheduled(type, true)

        Log.i("NotificationScheduler", "Notification scheduled successfully for type: $type")
    }

    private fun isNotificationScheduled(type: String): Boolean {
        return prefs.getBoolean(type, false)
    }

    private fun setNotificationScheduled(type: String, isScheduled: Boolean) {
        prefs.edit().putBoolean(type, isScheduled).apply()
    }

    fun cancelNotification(type: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, type.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.cancel(pendingIntent)
        setNotificationScheduled(type, false)
    }
}
