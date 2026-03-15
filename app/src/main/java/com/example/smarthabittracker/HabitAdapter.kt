package com.example.smarthabittracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthabittracker.data.Habit

class HabitAdapter(
    private var habits: List<Habit>,
    private val onHabitChecked: (Habit, Boolean) -> Unit,
    private val onHabitDeleted: (Habit) -> Unit
) : RecyclerView.Adapter<HabitAdapter.HabitViewHolder>() {

    class HabitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvHabitName: TextView = itemView.findViewById(R.id.tvHabitName)
        val tvHabitDescription: TextView = itemView.findViewById(R.id.tvHabitDescription)
        val tvStreak: TextView = itemView.findViewById(R.id.tvStreak)
        val cbHabit: CheckBox = itemView.findViewById(R.id.cbHabit)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)
        val ivHabitIcon: ImageView = itemView.findViewById(R.id.ivHabitIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_habit, parent, false)
        return HabitViewHolder(view)
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        val habit = habits[position]
        holder.tvHabitName.text = habit.habit_name
        holder.tvHabitDescription.text = habit.description
        holder.tvStreak.text = "Streak: 0 days" 

        // Set Icon based on name
        when(habit.habit_name.lowercase()) {
            "drink water" -> holder.ivHabitIcon.setImageResource(android.R.drawable.ic_menu_edit)
            "meditate" -> holder.ivHabitIcon.setImageResource(android.R.drawable.ic_lock_idle_alarm)
            "read book" -> holder.ivHabitIcon.setImageResource(android.R.drawable.ic_menu_search)
            else -> holder.ivHabitIcon.setImageResource(android.R.drawable.ic_menu_agenda)
        }

        holder.cbHabit.setOnCheckedChangeListener(null)
        holder.cbHabit.isChecked = false 

        holder.cbHabit.setOnCheckedChangeListener { _, isChecked ->
            onHabitChecked(habit, isChecked)
        }

        holder.btnDelete.setOnClickListener {
            onHabitDeleted(habit)
        }
    }

    override fun getItemCount(): Int = habits.size

    fun updateHabits(newHabits: List<Habit>) {
        this.habits = newHabits
        notifyDataSetChanged()
    }
}
