package com.example.shiftsaver.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.shiftsaver.data.DatabaseProvider
import com.example.shiftsaver.data.entity.MonthEntity
import kotlinx.coroutines.launch

class MonthListViewModel(application: Application) : AndroidViewModel(application) {
    private val db = DatabaseProvider.getDatabase(application)
    private val monthDao = db.monthDao()
    private val shiftDao = db.shiftDao()

    val allMonths: LiveData<List<MonthEntity>> = monthDao.getAllMonths().asLiveData()

    fun addMonth(year: Int, month: Int) {
        viewModelScope.launch {
            val newMonth = MonthEntity(year = year, month = month)
            monthDao.insert(newMonth)
        }
    }
    fun delMonth(monthId: Int){
        viewModelScope.launch {
            monthDao.deleteMonthByMonth(monthId)
            shiftDao.deleteAllForMonth(monthId)
        }
    }

    suspend fun getMonthByNameDao(month: Int, year: Int): MonthEntity? {
        return monthDao.getMonthByName(month, year)
    }
}
