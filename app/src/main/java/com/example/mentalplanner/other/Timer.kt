package com.example.mentalplanner.other

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations

class Timer(var targetSecondsCount: Int) {
    private var _elapsedSecondsCount = MutableLiveData(0)
    val elapsedSeconds = Transformations.map(_elapsedSecondsCount) { it % 60 }
    val elapsedMinutes = Transformations.map(_elapsedSecondsCount) { it / 60 }

    private var _handler = Handler(Looper.getMainLooper())
    private lateinit var _runnable: Runnable

    fun start() {
        _runnable = Runnable {
            if (_elapsedSecondsCount.value!! < targetSecondsCount) {
                _elapsedSecondsCount.value = _elapsedSecondsCount.value!!.inc()
                _handler.postDelayed(_runnable, 1000)
            } else {
                stop()
            }
        }

        _handler.postDelayed(_runnable, 1000)
    }

    fun stop() {
        _handler.removeCallbacks(_runnable)
    }

    fun reset() {
        _elapsedSecondsCount.value = 0
    }
}