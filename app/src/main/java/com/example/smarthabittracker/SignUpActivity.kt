package com.example.smarthabittracker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smarthabittracker.data.AppDatabase
import com.example.smarthabittracker.data.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val etFullName = findViewById<EditText>(R.id.etFullName)
        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnSignUp = findViewById<Button>(R.id.btnSignUp)
        val tvBackToLogin = findViewById<TextView>(R.id.tvBackToLogin)

        val db = AppDatabase.getDatabase(this)

        btnSignUp.setOnClickListener {
            val fullName = etFullName.text.toString()
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty() && fullName.isNotEmpty()) {
                CoroutineScope(Dispatchers.IO).launch {
                    val user = User(username, fullName, password)
                    db.habitDao().insertUser(user)
                    
                    withContext(Dispatchers.Main) {
                        val sharedPref = getSharedPreferences("HabitusPrefs", Context.MODE_PRIVATE)
                        with(sharedPref.edit()) {
                            putBoolean("isLoggedIn", true)
                            putString("username", username)
                            putString("fullName", fullName)
                            apply()
                        }

                        Toast.makeText(this@SignUpActivity, "Account created successfully!", Toast.LENGTH_SHORT).show()
                        
                        val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }
                }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        tvBackToLogin.setOnClickListener {
            finish()
        }
    }
}
