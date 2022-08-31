package com.example.waterreminder.receiver

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.waterreminder.utils.AlarmUtils
import com.example.waterreminder.utils.NotificationUtils
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AlarmReceiver: BroadcastReceiver(),KoinComponent {

    private val notificationUtils: NotificationUtils by inject()
    private val alarmUtils: AlarmUtils by inject()

    override fun onReceive(context: Context, intent: Intent) {
        when(intent.action) {
            AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED -> {
                alarmUtils.scheduleNewExactAlarm()
            }
            "REMINDER" -> {
                notificationUtils.showNotification()
                alarmUtils.scheduleNewExactAlarm()
            }
            "android.intent.action.BOOT_COMPLETED" -> {
                alarmUtils.scheduleNewExactAlarm()
            }
            }
    }
}