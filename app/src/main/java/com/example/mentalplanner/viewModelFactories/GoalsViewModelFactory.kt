package com.example.mentalplanner.viewModelFactories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mentalplanner.database.HabitDao
import com.example.mentalplanner.viewModels.GoalsViewModel

class GoalsViewModelFactory(
    private val habitDao: HabitDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GoalsViewModel::class.java)) {
            return GoalsViewModel(habitDao, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}