package com.example.shiftsaver.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "shifts")
data class ShiftEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val monthId: Int,
    val date: String,
    val startTime: String,
    val endTime: String,
    val totalTime: String
)