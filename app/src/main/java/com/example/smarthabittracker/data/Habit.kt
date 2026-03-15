package com.example.smarthabittracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true)
    val habit_id: Int = 0,
    val habit_name: String,
    val description: String,
    val created_date: String,
    val is_active: Int = 1, // 1 for active, 0 for inactive
    val owner: String = "Habitus User" // Added to separate habits by user
)
