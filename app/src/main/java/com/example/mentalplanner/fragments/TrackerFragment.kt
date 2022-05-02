package com.example.mentalplanner.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.mentalplanner.R
import com.example.mentalplanner.databinding.FragmentTrackerBinding
import com.example.mentalplanner.viewModels.TrackerViewModel

class TrackerFragment : Fragment() {

    private lateinit var binding: FragmentTrackerBinding

    private val viewModel: TrackerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_tracker, container, false
        )
        return binding.root
    }

}