package com.example.dataemail.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class Users(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val username: String,
    val email: String,
    val phone: String,
    val password: String,
    val profilePhotoUrl: String?,
    val role: String,
)
