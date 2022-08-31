package com.example.waterreminder.ui

import androidx.lifecycle.*
import com.example.waterreminder.datastore.DataStoreRepository
import com.example.waterreminder.models.Drink
import com.example.waterreminder.utils.AlarmUtils
import com.example.waterreminder.repository.HistoryRepository
import com.example.waterreminder.models.ReminderForRecycle
import com.example.waterreminder.repository.ReminderRepository
import com.example.waterreminder.models.History
import com.example.waterreminder.models.Reminder
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class ReminderViewModel(
    private val mReminderRepository: ReminderRepository,
    private val mHistoryRepository: HistoryRepository,
    private val mDataStoreRepository: DataStoreRepository,
    private val alarmUtils: AlarmUtils
) : ViewModel() {

    private var todayHistory: History? = null
    val remindersForRecycler: MutableLiveData<MutableList<ReminderForRecycle>> by lazy {
        MutableLiveData<MutableList<ReminderForRecycle>>()
    }

    val waterOfDay: MutableLiveData<Triple<Int, Int, Int>> by lazy {
        MutableLiveData<Triple<Int, Int, Int>>()
    }

    lateinit var userName: LiveData<String>

    val goalOfDay: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }


    init {
        viewModelScope.launch {
            val instant = Clock.System.now()
            val today = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
            userName = mDataStoreRepository.userNameFlow.asLiveData()

            launch {
                mDataStoreRepository.goalOfDayFlow.collect { newGoal ->
                    goalOfDay.postValue(newGoal)
                    todayHistory?.let {
                        it.goalOfDay = newGoal
                        mHistoryRepository.updateHistory(it)
                    }
                }
            }

            launch {
                mReminderRepository.allReminders.combine(
                    mHistoryRepository.getHistory(
                        today.dayOfMonth,
                        today.monthNumber,
                        today.year
                    )
                ) { reminders, history ->
                    if (history != null) {
                        todayHistory = history
                        updateListReminder(reminders, history)
                        updateTodayProgress(history)

                    } else {
                        todayHistory = History(0, today.dayOfMonth, today.monthNumber, today.year, mutableListOf(), 0, mDataStoreRepository.goalOfDayFlow.first())
                        mHistoryRepository.saveHistory(todayHistory!!)
                    }
                }.collect()
            }
        }
    }

    suspend fun isFirstAccess():Boolean = mDataStoreRepository.firstAccess.first()

    fun updateTodayProgress(history: History) {
        val newWaterOfDay =
            Triple(history.waterOfDay, history.goalOfDay, history.markedReminders.size)
        waterOfDay.postValue(newWaterOfDay)
    }

    fun updateListReminder(reminders: List<Reminder>, history: History) {
        val listAux = mutableListOf<ReminderForRecycle>()
        for (reminder in reminders) {
            val reminderForRecycler =
                ReminderForRecycle(reminder, reminder.id in history.markedReminders)
            listAux.add(reminderForRecycler)
        }

        remindersForRecycler.postValue(listAux)
    }

    fun reminderClicked(reminderForRecycle: ReminderForRecycle) {
        viewModelScope.launch {
            if (reminderForRecycle.marked) {
                todayHistory?.let {
                    it.markedReminders.remove(reminderForRecycle.reminder.id)
                    mHistoryRepository.updateHistory(it)
                }

            } else {
                todayHistory?.let {
                    it.markedReminders.add(reminderForRecycle.reminder.id)
                    mHistoryRepository.updateHistory(it)
                }
            }
        }
    }

    fun saveReminder(reminder: Reminder) {
        viewModelScope.launch {
            val id = mReminderRepository.saveReminder(reminder)
            alarmUtils.scheduleNewExactAlarm()
        }
    }


    fun deleteReminder(reminder: Reminder) = viewModelScope.launch {
        mReminderRepository.deleteReminder(reminder)
        deleteReminderFromTodayHistory(reminder)
        alarmUtils.scheduleNewExactAlarm()
    }

    fun drink(drink: Drink) {
        viewModelScope.launch {
            todayHistory?.let {
                it.waterOfDay += drink.size
                mHistoryRepository.updateHistory(it)
            }
        }
    }

    fun removeDrink(mls: Int) {
        viewModelScope.launch {
            todayHistory?.let {
                if(it.waterOfDay-mls > 0) {
                    it.waterOfDay -= mls
                    mHistoryRepository.updateHistory(it)
                } else {
                    it.waterOfDay = 0
                    mHistoryRepository.updateHistory(it)
                }
            }
        }
    }

    private suspend fun deleteReminderFromTodayHistory(reminder: Reminder) {
        val history = todayHistory
        history?.let {
            it.markedReminders.remove(reminder.id)
            mHistoryRepository.updateHistory(history)
        }

    }


    fun saveUserName(userName: String) {
        viewModelScope.launch {
            mDataStoreRepository.saveUserName(userName)
        }
    }

    fun saveGoalOfDay(goalOfDay: Int) {
        viewModelScope.launch {
            mDataStoreRepository.saveGoalOfDay(goalOfDay)
        }
    }

    fun saveFirstAccess(firstAccess: Boolean) {
        viewModelScope.launch {
            mDataStoreRepository.saveFirstAccess(firstAccess)
        }
    }


//    fun getHistory(day:Int,month:Int,year:Int):History{
//        viewModelScope.launch {
//            val history = mHistoryRepository.getHistory(day,month,year)
//            if (history == null){
//                mHistoryRepository.saveHistory(History(0,day,month,year,listOf(), listOf()))
//            }
//            val newHistory = mHistoryRepository.getHistory(day,month,year)
//        }
//    }


}