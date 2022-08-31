package com.example.waterreminder.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {

    object Keys {
        val USER_NAME = stringPreferencesKey("username")
        val GOAL_OF_DAY = intPreferencesKey("goal_of_day")
        val FIRST_ACCESS = booleanPreferencesKey("first_access")
    }

    val userNameFlow:Flow<String>
    val goalOfDayFlow:Flow<Int>
    val firstAccess:Flow<Boolean>

    suspend fun saveUserName(userName:String)

    suspend fun saveGoalOfDay(goalOfDay:Int)

    suspend fun saveFirstAccess(firstAccess:Boolean)



}