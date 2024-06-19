package com.example.budgetbonsai.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.budgetbonsai.R
import java.util.concurrent.TimeUnit

class NotificationWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        showNotification("Time to Save!", "Don't forget to save for your wishlist!")
        return Result.success()
    }

    private fun showNotification(title: String, message: String) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "default"
            val channel = NotificationChannel(channelId, "Default", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, "default")
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .build()

        notificationManager.notify(1, notification)
    }

    companion object {
        fun scheduleNotification(context: Context, type: String) {
            val interval = when (type) {
                "Daily" -> 1L
                "Weekly" -> 7L
                "Monthly" -> 30L
                else -> 1L
            }

            val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(interval, TimeUnit.DAYS)
                .build()

            WorkManager.getInstance(context).enqueue(workRequest)
        }
    }
}
