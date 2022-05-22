package com.example.mentalplanner.viewModelFactories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mentalplanner.database.HabitDao
import com.example.mentalplanner.viewModels.HabitCreationViewModel

class HabitCreationViewModelFactory(
    private val habitDao: HabitDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HabitCreationViewModel::class.java)) {
            return HabitCreationViewModel(habitDao, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}