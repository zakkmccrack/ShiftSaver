package com.example.shiftsaver.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Year

@Entity(tableName = "months")
data class MonthEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val year: Int,
    val month: Int
)