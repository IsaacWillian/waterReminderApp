package com.example.waterreminder.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import com.example.waterreminder.receiver.AlarmReceiver
import com.example.waterreminder.repository.ReminderRepository
import com.example.waterreminder.models.Reminder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AlarmUtils(val context: Context):KoinComponent {

    val mReminderRepository: ReminderRepository by inject()

    fun createAlarm(minutesToNextReminder:Int){
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, AlarmReceiver::class.java)
            intent.action = "REMINDER"
            val pendingIntent =
                PendingIntent.getBroadcast(context,0, intent, PendingIntent.FLAG_MUTABLE)
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP,SystemClock.elapsedRealtime() + minutesToNextReminder * 60 * 1000,pendingIntent
            )
    }

    fun scheduleNewExactAlarm(){
        GlobalScope.launch {
            val currentReminders = mReminderRepository.allReminders.collect(){
                var minDiff = Int.MAX_VALUE
                var nextReminder: Reminder? = null
                for (reminder in it){
                    val now = Clock.System.now()
                    val date = now.toLocalDateTime(TimeZone.currentSystemDefault())
                    val currentInstant = date.hour * 60 + date.minute
                    var reminderInstant = reminder.hour * 60 + reminder.minutes
                    if((reminder.hour<date.hour) || (date.hour == reminder.hour && reminder.minutes < date.minute) || (date.hour == reminder.hour && reminder.minutes == date.minute) ) {
                        reminderInstant += 24*60
                    }
                    val diffTime = reminderInstant - currentInstant
                    if(diffTime<minDiff){
                        minDiff = diffTime
                        nextReminder = reminder
                    }
                    }
                nextReminder?.let {
                    createAlarm(minDiff)
                }

            }


        }

    }


    fun cancelAlarm(reminderId:Long){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingToCancel = PendingIntent.getService(context,
            reminderId.toInt(),intent,PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE)
        //TODO Não está encontrando o pending intent
        if (pendingToCancel != null && alarmManager !=null){
            alarmManager.cancel(pendingToCancel)
        }


    }

}