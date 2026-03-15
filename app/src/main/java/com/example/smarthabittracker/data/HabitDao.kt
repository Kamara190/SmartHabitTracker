package com.example.smarthabittracker.data

import androidx.room.*

@Dao
interface HabitDao {
    @Insert
    suspend fun insertHabit(habit: Habit)

    @Update
    suspend fun updateHabit(habit: Habit)

    @Delete
    suspend fun deleteHabit(habit: Habit)

    @Query("SELECT * FROM habits WHERE owner = :username AND is_active = 1")
    suspend fun getHabitsByOwner(username: String): List<Habit>

    @Query("SELECT * FROM habits WHERE is_active = 1")
    suspend fun getAllActiveHabits(): List<Habit>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgress(progress: HabitProgress)

    @Query("SELECT * FROM habit_progress WHERE habit_id = :habitId")
    suspend fun getProgressForHabit(habitId: Int): List<HabitProgress>

    @Query("SELECT * FROM habit_progress WHERE date = :date")
    suspend fun getProgressByDate(date: String): List<HabitProgress>

    // User Operations
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    suspend fun getUser(username: String, password: String): User?
}
