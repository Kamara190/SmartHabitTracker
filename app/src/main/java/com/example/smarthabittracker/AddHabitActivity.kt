package com.example.smarthabittracker

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smarthabittracker.data.AppDatabase
import com.example.smarthabittracker.data.Habit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddHabitActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private var username: String = "Habitus User"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_habit)

        db = AppDatabase.getDatabase(this)

        // Get current logged in user
        val sharedPref = getSharedPreferences("HabitusPrefs", Context.MODE_PRIVATE)
        username = sharedPref.getString("username", "Habitus User") ?: "Habitus User"

        val etHabitName = findViewById<EditText>(R.id.etHabitName)
        val etDescription = findViewById<EditText>(R.id.etDescription)
        val btnSaveHabit = findViewById<Button>(R.id.btnSaveHabit)

        btnSaveHabit.setOnClickListener {
            val name = etHabitName.text.toString()
            val description = etDescription.text.toString()

            if (name.isNotEmpty()) {
                saveHabit(name, description)
            } else {
                Toast.makeText(this, "Please enter a habit name", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveHabit(name: String, description: String) {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = sdf.format(Date())

        val habit = Habit(
            habit_name = name,
            description = description,
            created_date = currentDate,
            is_active = 1,
            owner = username // Linking the habit to the current user
        )

        CoroutineScope(Dispatchers.IO).launch {
            db.habitDao().insertHabit(habit)
            withContext(Dispatchers.Main) {
                Toast.makeText(this@AddHabitActivity, "Habit saved!", Toast.LENGTH_SHORT).show()
                finish() // Go back to MainActivity
            }
        }
    }
}
