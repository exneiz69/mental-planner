package com.example.mentalplanner.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mentalplanner.other.Timer

class TimerViewModel : ViewModel() {
    var timer = Timer(0)
        private set

    val selectedMinutes = MutableLiveData(10)
    val selectedSeconds = MutableLiveData(0)

    fun start() {
        timer.targetSecondsCount = selectedMinutes.value!!.times(60) + selectedSeconds.value!!
        timer.start()
    }

    fun stop() {
        timer.stop()
    }

    fun reset() {
        timer.targetSecondsCount = selectedMinutes.value!!.times(60) + selectedSeconds.value!!
        timer.reset()
    }
}