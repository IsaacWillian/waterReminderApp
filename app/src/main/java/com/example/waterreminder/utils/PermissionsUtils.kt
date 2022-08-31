package com.example.waterreminder.utils

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.AlertDialog
import android.content.Context
import android.content.Context.POWER_SERVICE
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import androidx.core.content.ContextCompat.getSystemService
import com.example.waterreminder.R


class PermissionsUtils(val context: Context) {

    @SuppressLint("NewApi")
    fun checkExactAlarmPermission(): Boolean {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        return alarmManager.canScheduleExactAlarms()
    }

    @SuppressLint("NewApi")
    fun showAlertDialogPermission() {
        val intent = Intent().apply {
            action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }

    fun checkBatteryPermission(): Boolean {
        val packageName = context.packageName
        val powerManager = context.getSystemService(POWER_SERVICE) as PowerManager?
        return powerManager!!.isIgnoringBatteryOptimizations(packageName)
    }

    @SuppressLint("BatteryLife")
    fun showBatteryPermissionDialog() {
        val intent = Intent().apply {
            action = Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }
}