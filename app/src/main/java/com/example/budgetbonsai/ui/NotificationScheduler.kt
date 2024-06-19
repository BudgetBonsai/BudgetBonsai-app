package com.example.budgetbonsai.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

class NotificationScheduler(private val context: Context) {

    fun scheduleNotification(type: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, type.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val intervalMillis = when (type) {
            "Daily" -> AlarmManager.INTERVAL_DAY
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
    }

    fun cancelNotification(type: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, type.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.cancel(pendingIntent)
    }
}