package com.example.mentalplanner.viewModels

import android.app.Application
import androidx.lifecycle.*
import com.example.mentalplanner.R
import com.example.mentalplanner.database.*
import kotlinx.coroutines.*
import java.util.*

class HabitCreationViewModel(
    private val habitDao: HabitDao,
    application: Application
) : AndroidViewModel(application) {
    val newHabitName = MutableLiveData<String>()
    val selectedIcon = MutableLiveData<Int>()
    val selectedHabitTimePeriod = MutableLiveData<Int>()
    val selectedIconId = Transformations.map(selectedIcon) {
        when (it) {
            1 -> R.drawable.essential
            2 -> R.drawable.eat_drink_healthily
            3 -> R.drawable.keep_active
            4 -> R.drawable.ease_stress
            5 -> R.drawable.leisure_moments
            else -> R.drawable.custom
        }
    }
    val selectedHabitDays: LiveData<HabitDays>
        get() = _selectedHabitDays

    private val _application = application
    private var _initialHabit =
        Habit(preset = Preset.Custom, name = "My own habit", icon = R.drawable.custom)
    private var _loadedHabit = false
    private val _selectedHabitDays = MutableLiveData<HabitDays>()

    init {
        updateCreationState()
    }

    fun loadHabitForChange(habitId: Long) {
        viewModelScope.launch(Dispatchers.Main) {
            _initialHabit = habitDao.select(habitId)
            _initialHabit.completed = false
            _initialHabit.completedLastTime = Date(0L)
            _loadedHabit = true
            updateCreationState()
        }
    }

    fun selectDay(day: String, checked: Boolean) {
        when (day) {
            _application.getString(R.string.monday) -> if (_selectedHabitDays.value!!.monday != checked) {
                _selectedHabitDays.value!!.monday = checked
            }
            _application.getString(R.string.tuesday) -> if (_selectedHabitDays.value!!.tuesday != checked) {
                _selectedHabitDays.value!!.tuesday = checked
            }
            _application.getString(R.string.wednesday) -> if (_selectedHabitDays.value!!.wednesday != checked) {
                _selectedHabitDays.value!!.wednesday = checked
            }
            _application.getString(R.string.thursday) -> if (_selectedHabitDays.value!!.thursday != checked) {
                _selectedHabitDays.value!!.thursday = checked
            }
            _application.getString(R.string.friday) -> if (_selectedHabitDays.value!!.friday != checked) {
                _selectedHabitDays.value!!.friday = checked
            }
            _application.getString(R.string.saturday) -> if (_selectedHabitDays.value!!.saturday != checked) {
                _selectedHabitDays.value!!.saturday = checked
            }
            _application.getString(R.string.sunday) -> if (_selectedHabitDays.value!!.sunday != checked) {
                _selectedHabitDays.value!!.sunday = checked
            }
            else -> throw IllegalArgumentException("Wrong day name")
        }
    }

    fun isHabitValid() = newHabitName.value!!.isNotEmpty()

    fun saveHabit() {
        if (isHabitValid()) {
            val newHabit = _initialHabit.copy()
            newHabit.name = newHabitName.value!!
            newHabit.timePeriod = TimePeriod.fromInt(selectedHabitTimePeriod.value!!)
                ?: throw IllegalStateException("Selected time period is not valid")
            newHabit.icon = selectedIconId.value!!
            newHabit.habitDays = selectedHabitDays.value!!.copy()
            if (_loadedHabit) {
                if (newHabit == _initialHabit) {
                    if (!_initialHabit.active) {
                        viewModelScope.launch { habitDao.update(newHabit.copy(active = true)) }
                    }
                } else if (_initialHabit.preset == Preset.Custom) {
                    viewModelScope.launch { habitDao.update(newHabit) }
                } else {
                    viewModelScope.launch { habitDao.update(_initialHabit.copy(active = false)) }
                    viewModelScope.launch {
                        habitDao.insert(
                            newHabit.copy(
                                id = 0,
                                preset = Preset.Custom,
                                active = true
                            )
                        )
                    }
                }
            } else {
                viewModelScope.launch { habitDao.insert(newHabit.copy(active = true)) }
            }
        } else {
            throw IllegalStateException("Habit is not valid")
        }
    }

    private fun updateCreationState() {
        newHabitName.value = _initialHabit.name
        selectedIcon.value = when (_initialHabit.icon) {
            R.drawable.essential -> 1
            R.drawable.eat_drink_healthily -> 2
            R.drawable.keep_active -> 3
            R.drawable.ease_stress -> 4
            R.drawable.leisure_moments -> 5
            else -> 0
        }
        selectedHabitTimePeriod.value = _initialHabit.timePeriod.ordinal
        _selectedHabitDays.value = _initialHabit.habitDays.copy()
    }
}