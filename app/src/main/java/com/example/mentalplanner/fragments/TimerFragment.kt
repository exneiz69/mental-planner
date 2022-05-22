package com.example.mentalplanner.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mentalplanner.R
import com.example.mentalplanner.viewModels.TimerViewModel
import com.example.mentalplanner.databinding.FragmentTimerBinding

class TimerFragment : Fragment() {
    private lateinit var binding: FragmentTimerBinding

    private val viewModel: TimerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_timer, container, false
        )

        val application = requireNotNull(activity).application
        val minutesValues = Array(60) { it }
        ArrayAdapter(application, android.R.layout.simple_spinner_item, minutesValues).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.minutesSpinner.adapter = adapter
        }
        ArrayAdapter(application, android.R.layout.simple_spinner_item, minutesValues).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.secondsSpinner.adapter = adapter
        }

        binding.timerViewModel = viewModel
        binding.lifecycleOwner = this

        binding.startButton.setOnClickListener(::onStartButtonClick)
        binding.stopButton.setOnClickListener(::onStopButtonClick)
        binding.resetButton.setOnClickListener(::onResetButtonClick)
        binding.backButton.setOnClickListener(::onBackButtonClick)

        return binding.root
    }

    private fun onStartButtonClick(view: View) {
        viewModel.start()
    }

    private fun onStopButtonClick(view: View) {
        viewModel.stop()
    }

    private fun onResetButtonClick(view: View) {
        viewModel.reset()
    }

    private fun onBackButtonClick(view: View) {
        findNavController().navigate(TimerFragmentDirections.actionTimerFragmentToTrackerFragment())
    }
}