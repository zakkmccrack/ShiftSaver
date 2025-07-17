package com.example.shiftsaver.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.shiftsaver.data.entity.ShiftEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShiftDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(shift: ShiftEntity): Long

    @Query("SELECT * FROM shifts WHERE monthId = :monthId ORDER BY date ASC")
    fun getShiftsFromMonth(monthId: Int): Flow<List<ShiftEntity>>

    @Query("SELECT * FROM shifts WHERE monthId = :monthId ORDER BY date ASC")
    fun getShiftsForMonthLive(monthId: Int): LiveData<List<ShiftEntity>>

    @Delete
    suspend fun delete(shift: ShiftEntity)

    @Query("DELETE FROM shifts WHERE monthId = :monthId")
    suspend fun deleteAllForMonth(monthId: Int)
}