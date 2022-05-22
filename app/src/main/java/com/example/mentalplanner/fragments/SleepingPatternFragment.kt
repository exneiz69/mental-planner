package com.example.mentalplanner.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mentalplanner.R
import com.example.mentalplanner.databinding.FragmentSleepingPatternBinding
import com.example.mentalplanner.viewModels.SleepingPatternViewModel

class SleepingPatternFragment : Fragment() {
    private lateinit var binding: FragmentSleepingPatternBinding

    private val viewModel: SleepingPatternViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_sleeping_pattern, container, false
        )

        binding.sleepingPatternViewModel = viewModel
        binding.lifecycleOwner = this

        val application = requireNotNull(activity).application

        val hoursValues = Array(24) { it }
        val minutesValues = Array(60) { it }
        ArrayAdapter(application, android.R.layout.simple_spinner_item, hoursValues).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.wakeUpHoursSpinner.adapter = adapter
        }

        ArrayAdapter(application, android.R.layout.simple_spinner_item, minutesValues).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.wakeUpMinutesSpinner.adapter = adapter
        }

        ArrayAdapter(application, android.R.layout.simple_spinner_item, hoursValues).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.goBedHoursSpinner.adapter = adapter
        }

        ArrayAdapter(application, android.R.layout.simple_spinner_item, minutesValues).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.goBedMinutesSpinner.adapter = adapter
        }

        binding.nextButton.setOnClickListener(::onNextButtonClick)

        return binding.root
    }

    private fun onNextButtonClick(view: View) {
        if (viewModel.checkSleepingPattern()) {
            val sharedPreferences = requireContext().getSharedPreferences(
                getString(R.string.main_preferences),
                Context.MODE_PRIVATE
            )
            with(sharedPreferences.edit()) {
                putInt(
                    getString(com.example.mentalplanner.R.string.wake_up_hours_key),
                    viewModel.selectedWakeUpHours.value!!
                )
                apply()
            }
            with(sharedPreferences.edit()) {
                putInt(
                    getString(com.example.mentalplanner.R.string.wake_up_minutes_key),
                    viewModel.selectedWakeUpMinutes.value!!
                )
                apply()
            }
            with(sharedPreferences.edit()) {
                putInt(
                    getString(com.example.mentalplanner.R.string.go_bed_hours_key),
                    viewModel.selectedGoBedHours.value!!
                )
                apply()
            }
            with(sharedPreferences.edit()) {
                putInt(
                    getString(com.example.mentalplanner.R.string.go_bed_minutes_key),
                    viewModel.selectedGoBedMinutes.value!!
                )
                apply()
            }
            findNavController().navigate(SleepingPatternFragmentDirections.actionSleepingPatternFragmentToGoalsFragment())
        } else {
            Toast.makeText(context, "Wrong Sleeping Pattern", Toast.LENGTH_SHORT).show()
        }
    }
}