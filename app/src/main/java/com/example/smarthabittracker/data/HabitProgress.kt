package com.example.smarthabittracker.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "habit_progress",
    foreignKeys = [
        ForeignKey(
            entity = Habit::class,
            parentColumns = ["habit_id"],
            childColumns = ["habit_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class HabitProgress(
    @PrimaryKey(autoGenerate = true)
    val progress_id: Int = 0,
    val habit_id: Int,
    val date: String,
    val status: Int // 1 for Completed, 0 for Not Completed
)
