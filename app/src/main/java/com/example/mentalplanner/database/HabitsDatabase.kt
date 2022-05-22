package com.example.mentalplanner.database

import android.content.Context
import android.widget.Toast
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

@Database(entities = [Habit::class], version = 1, exportSchema = false)
abstract class HabitsDatabase : RoomDatabase() {
    abstract val habitDao: HabitDao

    companion object {
        private const val DATABASE_NAME = "HabitsDatabase"

        @Volatile
        private var INSTANCE: HabitsDatabase? = null

        fun getInstance(context: Context): HabitsDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        HabitsDatabase::class.java,
                        DATABASE_NAME
                    )
                        .fallbackToDestructiveMigration()
                        .createFromAsset("databases/$DATABASE_NAME.db")
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}