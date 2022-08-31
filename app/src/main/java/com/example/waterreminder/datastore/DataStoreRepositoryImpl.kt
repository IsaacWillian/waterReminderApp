package com.example.waterreminder.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreRepositoryImpl(val dataStore:DataStore<Preferences>):DataStoreRepository {
    override val userNameFlow: Flow<String>
        get() = dataStore.data.map{ preferences ->
            preferences[DataStoreRepository.Keys.USER_NAME] ?: ""
        }

    override val goalOfDayFlow: Flow<Int>
        get() = dataStore.data.map{ preferences ->
                preferences[DataStoreRepository.Keys.GOAL_OF_DAY] ?: 2000
        }

    override val firstAccess: Flow<Boolean>
        get() = dataStore.data.map{ preferences ->
            preferences[DataStoreRepository.Keys.FIRST_ACCESS] ?: true
        }

    override suspend fun saveUserName(userName: String) {
        dataStore.edit { preferences ->
            preferences[DataStoreRepository.Keys.USER_NAME] = userName
        }
    }

    override suspend fun saveGoalOfDay(goalOfDay: Int) {
        dataStore.edit { preferences ->
            preferences[DataStoreRepository.Keys.GOAL_OF_DAY] = goalOfDay
        }
    }

    override suspend fun saveFirstAccess(firstAccess: Boolean) {
        dataStore.edit{ preferences ->
            preferences[DataStoreRepository.Keys.FIRST_ACCESS] = firstAccess
        }
    }

}