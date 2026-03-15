package com.example.smarthabittracker

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.smarthabittracker.data.AppDatabase
import com.google.android.material.button.MaterialButton
import com.google.android.material.switchmaterial.SwitchMaterial
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        val switchDarkMode = view.findViewById<SwitchMaterial>(R.id.switchDarkMode)
        val btnResetData = view.findViewById<MaterialButton>(R.id.btnResetData)
        val btnLogout = view.findViewById<MaterialButton>(R.id.btnLogout)
        val btnPersonalInfo = view.findViewById<MaterialButton>(R.id.btnPersonalInfo)
        val btnNotifications = view.findViewById<MaterialButton>(R.id.btnNotifications)
        val btnPrivacyPolicy = view.findViewById<MaterialButton>(R.id.btnPrivacyPolicy)

        val sharedPref = requireActivity().getSharedPreferences("HabitusPrefs", Context.MODE_PRIVATE)
        val username = sharedPref.getString("username", "Habitus User")
        
        // Load the saved dark mode state
        val isDarkMode = sharedPref.getBoolean("isDarkMode", false)
        switchDarkMode.isChecked = isDarkMode

        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            // Save the choice
            sharedPref.edit().putBoolean("isDarkMode", isChecked).apply()
            
            // Apply the theme immediately
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        btnPersonalInfo.setOnClickListener {
            Toast.makeText(requireContext(), "User: $username\nEmail: $username@habitus.com", Toast.LENGTH_LONG).show()
        }

        btnNotifications.setOnClickListener {
            Toast.makeText(requireContext(), "Daily reminders enabled!", Toast.LENGTH_SHORT).show()
        }

        btnPrivacyPolicy.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"))
            startActivity(browserIntent)
        }

        btnResetData.setOnClickListener {
            val db = AppDatabase.getDatabase(requireContext())
            CoroutineScope(Dispatchers.IO).launch {
                db.clearAllTables()
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "All data erased!", Toast.LENGTH_SHORT).show()
                    activity?.recreate()
                }
            }
        }

        btnLogout.setOnClickListener {
            // Clear login status
            with(sharedPref.edit()) {
                putBoolean("isLoggedIn", false)
                apply()
            }
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        return view
    }
}
