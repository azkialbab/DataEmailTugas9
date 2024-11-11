package com.example.dataemail

import android.content.Context
import androidx.room.Room
import com.example.dataemail.database.UserDao
import com.example.dataemail.model.Users
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PrefManager(context: Context) {

    private val context: Context = context.applicationContext
    private val userDao: UserDao

    init {
        // Membuat database Room
        val db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "user_database"
        ).build()
        userDao = db.userDao()
    }

    // Menyimpan pengguna ke dalam Room
    suspend fun saveUser(username: String, email: String, phone: String, password: String, profilePhotoUrl: String, role: String) {
        val user = Users(
            username = username,
            email = email,
            phone = phone,
            password = password,
            profilePhotoUrl = profilePhotoUrl,
            role = role
        )
        withContext(Dispatchers.IO) {
            userDao.insert(user)
        }
    }

    // Mengambil pengguna berdasarkan username
    suspend fun getUser(username: String): Users? {
        return withContext(Dispatchers.IO) {
            userDao.getUserByUsername(username)
        }
    }

    // Login dengan memverifikasi username dan password
    suspend fun login(username: String, password: String): Users? {
        return withContext(Dispatchers.IO) {
            userDao.login(username, password)
        }
    }

    // Mendapatkan role pengguna berdasarkan username
    suspend fun getUserRole(username: String): String? {
        val user = getUser(username)
        return user?.role
    }

    // Menghapus data pengguna dari SharedPreferences (logout)
    fun logout() {
        val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.clear()
        editor.apply()
    }

    // Mendapatkan username pengguna yang sedang login dari SharedPreferences
    fun getLoggedInUsername(): String? {
        val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return sharedPref.getString("username", null)
    }

    // Menyimpan username pengguna yang sedang login ke SharedPreferences
    fun saveLoggedInUsername(username: String) {
        val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("username", username)
        editor.apply()
    }
}

