package com.example.smarthabittracker

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.smarthabittracker.data.AppDatabase
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class ProgressFragment : Fragment() {

    private lateinit var tvDailyStatus: TextView
    private lateinit var tvPercentage: TextView
    private lateinit var tvActiveHabits: TextView
    private lateinit var tvBestStreak: TextView
    private lateinit var barChart: BarChart
    private lateinit var db: AppDatabase
    private var username: String = "Habitus User"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_progress, container, false)
        
        db = AppDatabase.getDatabase(requireContext())
        
        tvDailyStatus = view.findViewById(R.id.tvDailyStatus)
        tvPercentage = view.findViewById(R.id.tvPercentage)
        tvActiveHabits = view.findViewById(R.id.tvActiveHabits)
        tvBestStreak = view.findViewById(R.id.tvBestStreak)
        barChart = view.findViewById(R.id.barChart)

        val sharedPref = requireActivity().getSharedPreferences("HabitusPrefs", Context.MODE_PRIVATE)
        username = sharedPref.getString("username", "Habitus User") ?: "Habitus User"

        loadStats()
        setupChart()
        
        return view
    }

    private fun loadStats() {
        CoroutineScope(Dispatchers.IO).launch {
            val habits = db.habitDao().getHabitsByOwner(username)
            val total = habits.size
            
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = sdf.format(Date())
            
            // Getting progress records for today's habits of the current user
            var completedCount = 0
            for (habit in habits) {
                val progress = db.habitDao().getProgressForHabit(habit.habit_id)
                if (progress.any { it.date == date && it.status == 1 }) {
                    completedCount++
                }
            }
            
            val percentage = if (total > 0) (completedCount * 100) / total else 0

            withContext(Dispatchers.Main) {
                tvDailyStatus.text = "Today: $completedCount/$total completed"
                tvPercentage.text = "$percentage%"
                tvActiveHabits.text = total.toString()
                tvBestStreak.text = if (completedCount > 0) "1" else "0" 
            }
        }
    }

    private fun setupChart() {
        val entries = ArrayList<BarEntry>()
        // This should ideally be calculated from history
        entries.add(BarEntry(0f, 0f))
        entries.add(BarEntry(1f, 0f))
        entries.add(BarEntry(2f, 0f))
        entries.add(BarEntry(3f, 0f))
        entries.add(BarEntry(4f, 0f))
        entries.add(BarEntry(5f, 0f))
        entries.add(BarEntry(6f, 0f))

        val dataSet = BarDataSet(entries, "Weekly History")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        dataSet.valueTextColor = Color.BLACK
        dataSet.valueTextSize = 12f

        val barData = BarData(dataSet)
        barChart.data = barData
        barChart.description.isEnabled = false
        barChart.animateY(1000)
        barChart.invalidate()
    }
}
