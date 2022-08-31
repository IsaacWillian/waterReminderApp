package com.example.waterreminder

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.logger.AndroidLogger
import org.koin.core.context.startKoin

class ReminderApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            AndroidLogger()
            androidContext(this@ReminderApplication)
            modules(
                userDb
            )
        }
    }
}