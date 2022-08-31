package com.example.waterreminder.repository

import com.example.waterreminder.models.History
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {

    suspend fun saveHistory(history: History)
    suspend fun updateHistory(history: History)
    fun getHistory(day:Int,month:Int,year:Int): Flow<History?>
    fun getHistorysByMonth(month:Int,year:Int):Flow<List<History?>>

}