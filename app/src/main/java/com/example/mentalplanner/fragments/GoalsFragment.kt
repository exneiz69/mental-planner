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
import com.example.mentalplanner.databinding.FragmentGoalsBinding
import com.example.mentalplanner.viewModels.GoalsViewModel

class GoalsFragment : Fragment() {

    private lateinit var binding: FragmentGoalsBinding

    private val viewModel: GoalsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_goals,container,false)

        binding.finishButton.setOnClickListener(::onFinishButtonClick)

        return binding.root
    }

    private fun onFinishButtonClick(view: View) {
        view.findNavController()
            .navigate(GoalsFragmentDirections.actionGoalsFragmentToTrackerFragment())
    }

}