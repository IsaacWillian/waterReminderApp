package com.example.waterreminder.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime

@Entity(tableName = "reminders")
class Reminder(@PrimaryKey(autoGenerate = true) val id:Long, val hour:Int, val minutes:Int) {

    fun hourToString():String{
        return if(hour > 9) return hour.toString() else "0$hour"

    }
     fun minutesToString():String{
         return if(minutes>9) return minutes.toString() else "0$minutes"
     }

}