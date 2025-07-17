package com.example.shiftsaver.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.shiftsaver.data.entity.MonthEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MonthDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(workMonth: MonthEntity): Long

    @Query("SELECT * FROM months ORDER BY year DESC, month DESC")
    fun getAllMonths(): Flow<List<MonthEntity>>

    @Query("SELECT * FROM months WHERE id = :id LIMIT 1")
    suspend fun getMonthById(id: Int): MonthEntity?

    @Delete
    suspend fun delete(workMonth: MonthEntity)

    @Query("DELETE FROM months WHERE id = :id")
    suspend fun deleteMonthByMonth(id: Int)
}