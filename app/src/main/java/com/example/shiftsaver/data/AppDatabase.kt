package com.example.shiftsaver.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.shiftsaver.data.dao.ShiftDao
import com.example.shiftsaver.data.dao.MonthDao
import com.example.shiftsaver.data.entity.ShiftEntity
import com.example.shiftsaver.data.entity.MonthEntity

@Database(entities = [ShiftEntity::class, MonthEntity::class], version = 2)
abstract class AppDatabase: RoomDatabase(){

    abstract fun shiftDao(): ShiftDao
    abstract fun monthDao(): MonthDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase ?= null

        fun getDatabase(context: Context): AppDatabase{
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "shift_saver_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
