package com.example.mentalplanner.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.mentalplanner.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class TrackerViewModel(
    private val habitDao: HabitDao,
    application: Application
) : AndroidViewModel(application) {
    val activatedHabits = habitDao.selectActive()

    fun updateHabitsCompletion(updateFinishedCallback: () -> Unit = {}) {
        viewModelScope.launch {
            val deferredCurrentActive = async(Dispatchers.IO) { habitDao.selectActiveList() }
            val currentActive = deferredCurrentActive.await()
            val job = launch {
                val today = Calendar.getInstance()
                today.time = Date()
                val dayOfWeek = today[Calendar.DAY_OF_WEEK]
                for (habit in currentActive) {
                    Log.i("updateHabitsCompletion", habit.name)
                    val currentHabitDays = habit.habitDays
                    val activeToday = when (dayOfWeek) {
                        Calendar.MONDAY -> currentHabitDays.monday
                        Calendar.TUESDAY -> currentHabitDays.tuesday
                        Calendar.WEDNESDAY -> currentHabitDays.wednesday
                        Calendar.THURSDAY -> currentHabitDays.thursday
                        Calendar.FRIDAY -> currentHabitDays.friday
                        Calendar.SATURDAY -> currentHabitDays.saturday
                        else -> currentHabitDays.sunday
                    }
                    if (activeToday) {
                        val completedLastTime = Calendar.getInstance()
                        completedLastTime.time = habit.completedLastTime
                        val notSameDay =
                            today[Calendar.DAY_OF_YEAR] != completedLastTime[Calendar.DAY_OF_YEAR] ||
                                    today[Calendar.YEAR] != completedLastTime[Calendar.YEAR]
                        if (notSameDay) {
                            withContext(Dispatchers.IO) { habitDao.update(habit.copy(completed = false)) }
                        }
                    }
                }
            }
            job.join()
            updateFinishedCallback()
        }
    }

    fun deactivate(habit: Habit) {
        if (habit.preset == Preset.Custom) {
            viewModelScope.launch { habitDao.delete(habit) }
        } else {
            viewModelScope.launch {
                habitDao.update(habit.copy(active = false, completed = false, completedLastTime = Date(0L)))
            }
        }
    }

    fun changeCompletedState(habit: Habit, completed: Boolean) {
        if (completed) {
            viewModelScope.launch { habitDao.update(habit.copy(completed = true, completedLastTime = Date())) }
        } else {
            viewModelScope.launch { habitDao.update(habit.copy(completed = false, completedLastTime = Date(0L))) }
        }
    }
}