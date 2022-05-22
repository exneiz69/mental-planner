package com.example.mentalplanner.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import com.example.mentalplanner.R
import com.example.mentalplanner.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val navHostFragment = binding.mainNavHostFragment.getFragment<NavHostFragment>()
        val navController = navHostFragment.navController
        val navGraph = navController.navInflater.inflate(R.navigation.navigation_graph)
        val sharedPreferences = getSharedPreferences(
            getString(R.string.main_preferences),
            Context.MODE_PRIVATE
        )
        if (!sharedPreferences.contains(getString(R.string.selected_initial_habit_key))) {
            with(sharedPreferences.edit()) {
                putBoolean(
                    getString(com.example.mentalplanner.R.string.selected_initial_habit_key),
                    false
                )
                commit()
            }
        }
        val selectedInitialHabit =
            sharedPreferences.getBoolean(getString(R.string.selected_initial_habit_key), false)
        if (selectedInitialHabit) {
            navGraph.setStartDestination(R.id.trackerFragment)
        } else {
            navGraph.setStartDestination(R.id.sleepingPatternFragment)
        }
        navController.graph = navGraph
    }
}