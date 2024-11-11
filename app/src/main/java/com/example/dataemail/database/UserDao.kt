package com.example.dataemail.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.dataemail.model.Users


@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: Users)

    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun getUserByUsername(username: String): Users?

    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    suspend fun login(username: String, password: String): Users?
}


