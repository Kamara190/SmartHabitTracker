package com.example.smarthabittracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val username: String,
    val fullName: String,
    val password: String
)
