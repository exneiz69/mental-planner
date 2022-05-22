package com.example.mentalplanner.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mentalplanner.database.*
import kotlinx.coroutines.launch

class GoalsViewModel(
    private val habitDao: HabitDao,
    application: Application
) : AndroidViewModel(application) {
    private val _currentHabits = MutableLiveData<List<Habit>>()

    val currentHabits: LiveData<List<Habit>>
        get() = _currentHabits

    var selectedHabit: Habit? = null
        private set

    fun changeCurrentGoals(preset: Preset) {
        viewModelScope.launch { _currentHabits.value = habitDao.select(preset) }
    }

    fun hasSelectedHabit() = selectedHabit != null

    fun selectHabit(name: String) {
        selectedHabit = currentHabits.value?.find { habit -> habit.name == name }
        if (selectedHabit == null) {
            throw IllegalArgumentException("Wrong habit name")
        }
    }

    fun unselectHabit() {
        selectedHabit = null
    }

    fun activateSelectedHabit() {
        if (hasSelectedHabit()) {
            selectedHabit!!.active = true
            viewModelScope.launch { habitDao.update(selectedHabit!!) }
        } else {
            throw IllegalStateException("Habit is not selected")
        }
    }
}