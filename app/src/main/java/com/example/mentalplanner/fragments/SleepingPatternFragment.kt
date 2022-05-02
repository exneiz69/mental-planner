package com.example.mentalplanner.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
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

        binding.nextButton.setOnClickListener(::onNextButtonClick)

        return binding.root
    }

    private fun onNextButtonClick(view: View) {
        view.findNavController()
            .navigate(SleepingPatternFragmentDirections.actionSleepingPatternFragmentToGoalsFragment())
    }
}