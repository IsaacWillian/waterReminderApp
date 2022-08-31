package com.example.waterreminder.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.waterreminder.R
import com.example.waterreminder.ui.MainActivity

class NotificationUtils(val context: Context){

    val CHANNEL_ID = "reminderWater"

    init {
    createNotificationChannel()
    }

    fun showNotification(){

        val notificationLayout = RemoteViews("com.example.waterreminder",R.layout.notification)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val largeIcon = BitmapFactory.decodeResource(context.resources,R.drawable.water_icon)
        val builder = NotificationCompat.Builder(context,CHANNEL_ID)
            .setSmallIcon(R.drawable.water_icon)
            .setLargeIcon(largeIcon)
            .setContentTitle("WaterReminder")
            .setContentText("Hora de tomar um copo de água")
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)

        with(NotificationManagerCompat.from(context)) {
            notify(5, builder.build())
        }


    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val descriptionText = context.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, descriptionText, importance)
            // Register the channel with the system
            val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

    }
    }
