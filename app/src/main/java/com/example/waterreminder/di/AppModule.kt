package com.example.waterreminder

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.example.waterreminder.datastore.DataStoreRepository
import com.example.waterreminder.datastore.DataStoreRepositoryImpl
import com.example.waterreminder.db.HistoryDao
import com.example.waterreminder.db.ReminderDao
import com.example.waterreminder.db.ReminderDataBase
import com.example.waterreminder.repository.HistoryRepository
import com.example.waterreminder.repository.HistoryRepositoryImpl
import com.example.waterreminder.repository.ReminderRepository
import com.example.waterreminder.repository.ReminderRepositoryImpl
import com.example.waterreminder.ui.HistoryViewModel
import com.example.waterreminder.ui.ReminderViewModel
import com.example.waterreminder.utils.AlarmUtils
import com.example.waterreminder.utils.NotificationUtils
import com.example.waterreminder.utils.PermissionsUtils
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val userDb = module {
    fun provideDataBase(application: Application): ReminderDataBase {
        return Room.databaseBuilder(application, ReminderDataBase::class.java,"reminder_database").build()
    }

    fun provideReminderDao(dataBase: ReminderDataBase): ReminderDao {
        return dataBase.reminderDao
    }

    fun provideHistoryDao(dataBase: ReminderDataBase): HistoryDao {
        return dataBase.historyDao
    }

    fun provideDataStore(context: Context) = PreferenceDataStoreFactory.create(produceFile = {context.preferencesDataStoreFile("UserPreferences")})


    single<DataStoreRepository>{ DataStoreRepositoryImpl(get())}
    single{ provideDataStore(androidContext())}
    single{ provideDataBase(androidApplication())    }
    single{ provideHistoryDao(get())}
    single{ provideReminderDao(get())}
    single<ReminderRepository>{ ReminderRepositoryImpl(get()) }
    single<HistoryRepository>{ HistoryRepositoryImpl(get()) }
    single{ NotificationUtils(androidContext()) }
    single { AlarmUtils(androidContext()) }
    single { PermissionsUtils(androidContext())}

    viewModel { ReminderViewModel(get(),get(),get(),get()) }
    viewModel{ HistoryViewModel(get(),get()) }

}