package com.example.mentalplanner.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mentalplanner.R
import com.example.mentalplanner.database.Habit
import com.example.mentalplanner.database.HabitsDatabase
import com.example.mentalplanner.database.Preset
import com.example.mentalplanner.databinding.FragmentGoalsBinding
import com.example.mentalplanner.viewModelFactories.GoalsViewModelFactory
import com.example.mentalplanner.viewModels.GoalsViewModel
import com.google.android.material.chip.Chip

class GoalsFragment : Fragment() {
    private lateinit var binding: FragmentGoalsBinding

    private lateinit var viewModel: GoalsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_goals, container, false
        )

        val application = requireNotNull(activity).application
        val habitDao = HabitsDatabase.getInstance(application).habitDao
        val viewModelFactory = GoalsViewModelFactory(habitDao, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(GoalsViewModel::class.java)

        binding.goalsViewModel = viewModel
        binding.lifecycleOwner = this

        binding.essentialButton.setOnClickListener(::onEssentialButtonClick)
        binding.eatDrinkHealthilyButton.setOnClickListener(::onEatDrinkHealthilyButtonClick)
        binding.keepActiveButton.setOnClickListener(::onKeepActiveButtonClick)
        binding.easeStressButton.setOnClickListener(::onEaseStressButtonClick)
        binding.leisureMomentsButton.setOnClickListener(::onLeisureMomentsButtonClick)
        binding.nextButton.setOnClickListener(::onNextButtonClick)

        viewModel.currentHabits.observe(viewLifecycleOwner, ::fillHabitsChipGroup)

        return binding.root
    }

    private fun onEssentialButtonClick(view: View) {
        viewModel.changeCurrentGoals(Preset.Essential)
    }

    private fun onEatDrinkHealthilyButtonClick(view: View) {
        viewModel.changeCurrentGoals(Preset.EatDrinkHealthily)
    }

    private fun onKeepActiveButtonClick(view: View) {
        viewModel.changeCurrentGoals(Preset.KeepActive)
    }

    private fun onEaseStressButtonClick(view: View) {
        viewModel.changeCurrentGoals(Preset.EaseStress)
    }

    private fun onLeisureMomentsButtonClick(view: View) {
        viewModel.changeCurrentGoals(Preset.LeisureMoments)
    }

    private fun onNextButtonClick(view: View) {
        if (viewModel.hasSelectedHabit()) {
            val sharedPreferences = requireContext().getSharedPreferences(
                getString(R.string.main_preferences),
                Context.MODE_PRIVATE
            )
            val selectedInitialHabit =
                sharedPreferences.getBoolean(getString(R.string.selected_initial_habit_key), false)
            if (selectedInitialHabit) {
                findNavController().navigate(
                    GoalsFragmentDirections.actionGoalsFragmentToHabitCreationFragment(
                        viewModel.selectedHabit!!.id,
                        false
                    )
                )
            } else {
                viewModel.activateSelectedHabit()
                with(sharedPreferences.edit()) {
                    putBoolean(
                        getString(com.example.mentalplanner.R.string.selected_initial_habit_key),
                        true
                    )
                    apply()
                }
                findNavController().navigate(GoalsFragmentDirections.actionGoalsFragmentToTrackerFragment())
            }
        } else {
            Toast.makeText(context, "Select Your Goal", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fillHabitsChipGroup(habits: List<Habit>) {
        val chipGroup = binding.habitsChipGroup
        chipGroup.removeAllViews()
        for (habit in habits) {
            val chip = Chip(context)
            chip.text = habit.name
            chip.isCheckable = true
            chipGroup.addView(chip)
            if (habit.name == viewModel.selectedHabit?.name) {
                chipGroup.check(chip.id)
            }
        }
        chipGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId != -1) {
                val chip: Chip = group.findViewById(checkedId)
                viewModel.selectHabit(chip.text as String)
            } else {
                viewModel.unselectHabit()
            }
        }

    }
}