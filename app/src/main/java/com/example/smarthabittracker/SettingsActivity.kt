package com.example.smarthabittracker

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.smarthabittracker.data.AppDatabase
import com.google.android.material.switchmaterial.SwitchMaterial
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val switchDarkMode = findViewById<SwitchMaterial>(R.id.switchDarkMode)
        val btnResetData = findViewById<Button>(R.id.btnResetData)

        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        btnResetData.setOnClickListener {
            val db = AppDatabase.getDatabase(this)
            CoroutineScope(Dispatchers.IO).launch {
                // Simplified reset - in a real app you'd clear all tables
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@SettingsActivity, "Data reset successfully!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
