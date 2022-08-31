package com.example.waterreminder.ui

import androidx.lifecycle.*
import com.example.waterreminder.datastore.DataStoreRepository
import com.example.waterreminder.models.History
import com.example.waterreminder.repository.HistoryRepository
import com.example.waterreminder.utils.DateUtils
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.*

class HistoryViewModel(private val mHistoryRepository: HistoryRepository, private val mDataStoreRepository: DataStoreRepository):ViewModel() {

    val historysByMonth: MutableLiveData<List<History?>> by lazy {
        MutableLiveData<List<History?>>()
    }

    val currentMonthAndYear:MutableLiveData<Pair<Int,Int>> by lazy {
        MutableLiveData<Pair<Int,Int>>()
    }


    init{
        viewModelScope.launch {
            val today = DateUtils.getToday()
            currentMonthAndYear.value = Pair(today.monthNumber, today.year)

        }
    }

    fun updateHistories() {
        viewModelScope.launch {
            val historys =  mHistoryRepository.getHistorysByMonth(currentMonthAndYear.value!!.first,currentMonthAndYear.value!!.second).first()
            val historysFilled = createDaysNonExistents(historys)
            historysByMonth.value = historysFilled
        }
    }

    private suspend fun createDaysNonExistents(historysExisting:List<History?>):List<History?>{
        val dayToCreate = (1..DateUtils.getMaxOfDayOfMonth(currentMonthAndYear.value!!.first,currentMonthAndYear.value!!.second)).toMutableList()
        for (history in historysExisting){
            dayToCreate.remove(history!!.day)
        }
        val historyMutable = historysExisting.toMutableList()
            historyMutable.addAll(dayToCreate.map { day -> History(0,day,currentMonthAndYear.value!!.first,currentMonthAndYear.value!!.second,
                mutableListOf(),0,mDataStoreRepository.goalOfDayFlow.first()) })

        return historyMutable.toList().sortedBy { history -> history!!.day  }
    }

    fun nextMonth(){
        viewModelScope.launch {
            currentMonthAndYear.value?.let{
                val month = it.first
                val year = it.second
                val newMonthAndYear = if (month == 12) Pair(1, year + 1) else Pair(month+1,year)
                currentMonthAndYear.value = newMonthAndYear
                updateHistories()

            }
        }
    }

    fun previousMonth(){
        viewModelScope.launch {
            currentMonthAndYear.value?.let{
                val month = it.first
                val year = it.second
                val newMonthAndYear = if (month == 1) Pair(12, year - 1) else Pair(month-1,year)
                currentMonthAndYear.value = newMonthAndYear
                updateHistories()

            }
        }
    }

}