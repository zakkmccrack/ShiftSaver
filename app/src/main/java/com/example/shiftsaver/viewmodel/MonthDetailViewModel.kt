package com.example.shiftsaver.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.*
import com.example.shiftsaver.data.dao.ShiftDao
import com.example.shiftsaver.data.entity.ShiftEntity
import kotlinx.coroutines.launch

class MonthDetailViewModel(private val shiftDao: ShiftDao) : ViewModel() {

    fun getShiftsForMonth(monthId: Int): LiveData<List<ShiftEntity>>{
        return shiftDao.getShiftsForMonthLive(monthId)
    }

    fun insertShift(shift: ShiftEntity) {
        viewModelScope.launch {
            shiftDao.insert(shift)
        }
    }

    fun deleteShift(shift: ShiftEntity){
        viewModelScope.launch {
            shiftDao.delete(shift)
        }
    }
}