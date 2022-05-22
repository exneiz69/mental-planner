package com.example.mentalplanner.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
abstract class HabitDao {
    @Insert
    abstract suspend fun insert(habit: Habit)

    @Update
    abstract suspend fun update(habit: Habit)

    @Delete
    abstract suspend fun delete(habit: Habit)

    @Query("SELECT * FROM Habits WHERE ID = :habitId")
    abstract suspend fun select(habitId: Long): Habit

    @Query("SELECT * FROM Habits WHERE Active = 1")
    abstract fun selectActive(): LiveData<List<Habit>>

    @Query("SELECT * FROM Habits WHERE Active = 1")
    abstract fun selectActiveList(): List<Habit>

    suspend fun select(preset: Preset): List<Habit> = select(preset.name)

    @Query("SELECT * FROM Habits WHERE Preset = :preset")
    protected abstract suspend fun select(preset: String): List<Habit>
}