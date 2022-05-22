package com.example.mentalplanner.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SleepingPatternViewModel : ViewModel() {
    val selectedWakeUpHours = MutableLiveData(8)
    val selectedWakeUpMinutes = MutableLiveData(0)
    val selectedGoBedHours = MutableLiveData(22)
    val selectedGoBedMinutes = MutableLiveData(0)

    fun checkSleepingPattern() = selectedGoBedHours.value!!.minus(selectedWakeUpHours.value!!) > 8
}