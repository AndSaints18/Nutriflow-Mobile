package com.nutriflow.mobile.ui.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class MealNotificationManager(private val context: Context) {

    companion object {
        const val CHANNEL_MEALS_ID = "nutriflow_meals_channel"
        const val CHANNEL_WATER_ID = "nutriflow_water_channel"
    }

    init {
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mealsChannel = NotificationChannel(
                CHANNEL_MEALS_ID,
                "Lembretes de Refeições",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notificações para lembretes de horários do plano alimentar"
            }

            val waterChannel = NotificationChannel(
                CHANNEL_WATER_ID,
                "Lembretes de Água",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notificações periódicas para hidratação"
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mealsChannel)
            notificationManager.createNotificationChannel(waterChannel)
        }
    }

    fun showMealNotification(title: String, message: String) {
        val builder = NotificationCompat.Builder(context, CHANNEL_MEALS_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(context)
        try {
            notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
        } catch (e: SecurityException) {
            // Ignore if permission not granted
        }
    }
}
