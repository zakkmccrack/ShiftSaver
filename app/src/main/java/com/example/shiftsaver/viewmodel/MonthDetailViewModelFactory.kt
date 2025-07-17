package com.example.shiftsaver.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shiftsaver.data.dao.ShiftDao


class MonthDetailViewModelFactory(private val shiftDao: ShiftDao) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MonthDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MonthDetailViewModel(shiftDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}