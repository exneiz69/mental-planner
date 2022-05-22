package com.example.mentalplanner.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mentalplanner.R
import com.example.mentalplanner.adapters.*
import com.example.mentalplanner.database.*
import com.example.mentalplanner.databinding.FragmentTrackerBinding
import com.example.mentalplanner.viewModelFactories.TrackerViewModelFactory
import com.example.mentalplanner.viewModels.TrackerViewModel

class TrackerFragment : Fragment() {
    private lateinit var binding: FragmentTrackerBinding

    private lateinit var viewModel: TrackerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_tracker, container, false
        )

        val application = requireNotNull(activity).application
        val habitDao = HabitsDatabase.getInstance(application).habitDao
        val viewModelFactory = TrackerViewModelFactory(habitDao, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(TrackerViewModel::class.java)

        binding.trackerViewModel = viewModel
        binding.lifecycleOwner = this

        val adapter = ActivatedHabitsAdapter(
            ActivatedHabitChangeButtonListener(::onActivatedHabitChangeButtonClick),
            ActivatedHabitDeleteButtonListener(viewModel::deactivate),
            CompletedCheckBoxListener(viewModel::changeCompletedState),
            ActivatedHabitTimerButtonListener(::onTimerButtonClick)
        )
        binding.activatedHabitsRecyclerView.adapter = adapter

        binding.addHabitButton.setOnClickListener(::onAddHabitButtonClick)
        binding.createHabitButton.setOnClickListener(::onCreateHabitButtonClick)

        viewModel.activatedHabits.observe(viewLifecycleOwner) { adapter.submitList(it) }

        viewModel.updateHabitsCompletion {
            viewModel.activatedHabits.observe(viewLifecycleOwner) { adapter.submitList(it) }
        }

        return binding.root
    }

    private fun onActivatedHabitChangeButtonClick(habit: Habit) {
        findNavController().navigate(
            TrackerFragmentDirections.actionTrackerFragmentToHabitCreationFragment(habit.id, false)
        )
    }

    private fun onAddHabitButtonClick(view: View) {
        findNavController().navigate(TrackerFragmentDirections.actionTrackerFragmentToGoalsFragment())
    }

    private fun onCreateHabitButtonClick(view: View) {
        findNavController().navigate(
            TrackerFragmentDirections.actionTrackerFragmentToHabitCreationFragment(0, true)
        )
    }

    private fun onTimerButtonClick(habit: Habit) {
        findNavController().navigate(
            TrackerFragmentDirections.actionTrackerFragmentToTimerFragment()
        )
    }
}