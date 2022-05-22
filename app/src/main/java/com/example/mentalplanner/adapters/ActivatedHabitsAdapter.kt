package com.example.mentalplanner.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mentalplanner.R
import com.example.mentalplanner.database.Habit
import com.example.mentalplanner.database.Preset
import com.example.mentalplanner.databinding.ItemActivatedHabitBinding

class ActivatedHabitsAdapter(
    private val changeButtonClickListener: ActivatedHabitChangeButtonListener,
    private val deleteButtonClickListener: ActivatedHabitDeleteButtonListener,
    private val completedCheckBoxListener: CompletedCheckBoxListener,
    private val timerButtonClickListener: ActivatedHabitTimerButtonListener
) :
    ListAdapter<Habit, ActivatedHabitsAdapter.ActivatedHabitViewHolder>(ActivatedHabitsDiffCallback()) {

    override fun onBindViewHolder(holder: ActivatedHabitViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(
            item,
            changeButtonClickListener,
            deleteButtonClickListener,
            completedCheckBoxListener,
            timerButtonClickListener
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivatedHabitViewHolder {
        return ActivatedHabitViewHolder.from(parent)
    }

    class ActivatedHabitViewHolder private constructor(val binding: ItemActivatedHabitBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: Habit,
            changeButtonClickListener: ActivatedHabitChangeButtonListener,
            deleteButtonClickListener: ActivatedHabitDeleteButtonListener,
            completedCheckBoxListener: CompletedCheckBoxListener,
            timerButtonClickListener: ActivatedHabitTimerButtonListener
        ) {
            binding.habit = item
            binding.habitIconImageView.setImageResource(item.icon)
            binding.completedCheckBox.isChecked = item.completed
            binding.changeButton.setOnClickListener { changeButtonClickListener.onClick(item) }
            binding.deleteButton.setOnClickListener { deleteButtonClickListener.onClick(item) }
            binding.completedCheckBox.setOnClickListener {
                completedCheckBoxListener.onClick(
                    item,
                    binding.completedCheckBox.isChecked
                )
            }
            binding.timerButton.setOnClickListener { timerButtonClickListener.onClick(item) }
        }

        companion object {
            fun from(parent: ViewGroup): ActivatedHabitViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemActivatedHabitBinding.inflate(layoutInflater, parent, false)
                return ActivatedHabitViewHolder(binding)
            }
        }
    }
}

class ActivatedHabitsDiffCallback : DiffUtil.ItemCallback<Habit>() {
    override fun areItemsTheSame(oldItem: Habit, newItem: Habit) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Habit, newItem: Habit) = oldItem == newItem
}

class ActivatedHabitChangeButtonListener(private val clickListener: (habit: Habit) -> Unit) {
    fun onClick(habit: Habit) = clickListener(habit)
}

class ActivatedHabitDeleteButtonListener(private val clickListener: (habit: Habit) -> Unit) {
    fun onClick(habit: Habit) = clickListener(habit)
}

class CompletedCheckBoxListener(private val clickListener: (habit: Habit, completed: Boolean) -> Unit) {
    fun onClick(habit: Habit, completed: Boolean) = clickListener(habit, completed)
}

class ActivatedHabitTimerButtonListener(private val clickListener: (habit: Habit) -> Unit) {
    fun onClick(habit: Habit) = clickListener(habit)
}