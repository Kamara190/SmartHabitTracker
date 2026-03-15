package com.example.smarthabittracker

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthabittracker.data.AppDatabase
import com.example.smarthabittracker.data.Habit
import com.example.smarthabittracker.data.HabitProgress
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var habitAdapter: HabitAdapter
    private lateinit var db: AppDatabase
    private lateinit var llEmptyState: LinearLayout
    private lateinit var rvHabits: RecyclerView
    private var username: String = "Habitus User"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        
        db = AppDatabase.getDatabase(requireContext())
        rvHabits = view.findViewById(R.id.rvHabits)
        llEmptyState = view.findViewById(R.id.llEmptyState)

        val sharedPref = requireActivity().getSharedPreferences("HabitusPrefs", Context.MODE_PRIVATE)
        username = sharedPref.getString("username", "Habitus User") ?: "Habitus User"

        habitAdapter = HabitAdapter(emptyList(), { habit, isChecked ->
            updateHabitProgress(habit, isChecked)
        }, { habit ->
            deleteHabit(habit)
        })

        rvHabits.layoutManager = LinearLayoutManager(context)
        rvHabits.adapter = habitAdapter

        ensureDefaultHabits()
        
        return view
    }

    private fun ensureDefaultHabits() {
        CoroutineScope(Dispatchers.IO).launch {
            val habits = db.habitDao().getHabitsByOwner(username)
            if (habits.isEmpty()) {
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val date = sdf.format(Date())
                val defaultHabits = listOf(
                    Habit(habit_name = "Read Book", description = "Read for 30 mins", created_date = date, owner = username),
                    Habit(habit_name = "Drink Water", description = "8 glasses a day", created_date = date, owner = username),
                    Habit(habit_name = "Meditate", description = "10 mins mindfulness", created_date = date, owner = username)
                )
                defaultHabits.forEach { db.habitDao().insertHabit(it) }
            }
            loadHabits()
        }
    }

    private fun updateHabitProgress(habit: Habit, isChecked: Boolean) {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = sdf.format(Date())
        val progress = HabitProgress(habit_id = habit.habit_id, date = date, status = if (isChecked) 1 else 0)
        
        CoroutineScope(Dispatchers.IO).launch {
            db.habitDao().insertProgress(progress)
            withContext(Dispatchers.Main) {
                val status = if (isChecked) "completed" else "unmarked"
                Toast.makeText(requireContext(), "${habit.habit_name} $status!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteHabit(habit: Habit) {
        CoroutineScope(Dispatchers.IO).launch {
            db.habitDao().deleteHabit(habit)
            loadHabits()
        }
    }

    private fun loadHabits() {
        CoroutineScope(Dispatchers.IO).launch {
            val habits = db.habitDao().getHabitsByOwner(username)
            withContext(Dispatchers.Main) {
                if (habits.isEmpty()) {
                    rvHabits.visibility = View.GONE
                    llEmptyState.visibility = View.VISIBLE
                } else {
                    rvHabits.visibility = View.VISIBLE
                    llEmptyState.visibility = View.GONE
                    habitAdapter.updateHabits(habits)
                }
            }
        }
    }
}
