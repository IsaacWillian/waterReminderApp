package com.example.waterreminder.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.waterreminder.db.HistoryDao
import com.example.waterreminder.db.ReminderDao
import com.example.waterreminder.models.History
import com.example.waterreminder.models.Reminder

@Database(entities = arrayOf(Reminder::class, History::class),version = 3,exportSchema = false)
abstract class ReminderDataBase:RoomDatabase() {
    abstract val reminderDao: ReminderDao
    abstract val historyDao: HistoryDao
}