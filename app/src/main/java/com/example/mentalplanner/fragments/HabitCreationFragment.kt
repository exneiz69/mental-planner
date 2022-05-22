package com.example.mentalplanner.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.mentalplanner.viewModels.HabitCreationViewModel
import com.example.mentalplanner.R
import com.example.mentalplanner.database.HabitDays
import com.example.mentalplanner.database.HabitsDatabase
import com.example.mentalplanner.databinding.FragmentHabitCreationBinding
import com.example.mentalplanner.viewModelFactories.HabitCreationViewModelFactory
import com.google.android.material.chip.Chip

class HabitCreationFragment : Fragment() {
    private lateinit var binding: FragmentHabitCreationBinding

    private lateinit var viewModel: HabitCreationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_habit_creation, container, false
        )

        val application = requireNotNull(activity).application
        val habitDao = HabitsDatabase.getInstance(application).habitDao
        val viewModelFactory = HabitCreationViewModelFactory(habitDao, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(HabitCreationViewModel::class.java)

        binding.habitCreationViewModel = viewModel
        binding.lifecycleOwner = this

        ArrayAdapter.createFromResource(
            application,
            R.array.habit_icon,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.habitIconSpinner.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            application,
            R.array.habit_time_period,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.habitTimePeriodSpinner.adapter = adapter
        }

        binding.saveButton.setOnClickListener(::onSaveButtonClick)

        binding.mondayChip.setOnCheckedChangeListener(::chipToHabitDays)
        binding.tuesdayChip.setOnCheckedChangeListener(::chipToHabitDays)
        binding.wednesdayChip.setOnCheckedChangeListener(::chipToHabitDays)
        binding.thursdayChip.setOnCheckedChangeListener(::chipToHabitDays)
        binding.fridayChip.setOnCheckedChangeListener(::chipToHabitDays)
        binding.saturdayChip.setOnCheckedChangeListener(::chipToHabitDays)
        binding.sundayChip.setOnCheckedChangeListener(::chipToHabitDays)

        viewModel.selectedIconId.observe(viewLifecycleOwner) { binding.habitIconImageView.setImageResource(it) }
        viewModel.selectedHabitDays.observe(viewLifecycleOwner) { habitDaysToChipGroup(it) }

        val args = HabitCreationFragmentArgs.fromBundle(requireArguments())
        if (!args.habitCreation) {
            viewModel.loadHabitForChange(args.habitID)
        }

        return binding.root
    }

    private fun onSaveButtonClick(view: View) {
        if (viewModel.isHabitValid()) {
            viewModel.saveHabit()
            findNavController().navigate(HabitCreationFragmentDirections.actionHabitCreationFragmentToTrackerFragment())
        } else {
            Toast.makeText(context, "Enter The Habit Name", Toast.LENGTH_SHORT).show()
        }
    }

    private fun habitDaysToChipGroup(habitDays: HabitDays) {
        binding.mondayChip.isChecked = habitDays.monday
        binding.tuesdayChip.isChecked = habitDays.tuesday
        binding.wednesdayChip.isChecked = habitDays.wednesday
        binding.thursdayChip.isChecked = habitDays.thursday
        binding.fridayChip.isChecked = habitDays.friday
        binding.saturdayChip.isChecked = habitDays.saturday
        binding.sundayChip.isChecked = habitDays.sunday
    }

    private fun chipToHabitDays(compoundButton: CompoundButton, checked: Boolean) {
        viewModel.selectDay(compoundButton.text as String, checked)
    }
}