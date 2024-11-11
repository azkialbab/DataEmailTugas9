package com.example.dataemail.admin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.dataemail.AppDatabase
import com.example.dataemail.PrefManager
import com.example.dataemail.databinding.ActivityDashboardBinding
import com.example.dataemail.admin.MovieCollection  // Pastikan import kelas yang benar

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var db: AppDatabase  // Untuk akses database jika diperlukan
    private lateinit var prefManager: PrefManager  // Untuk akses preferensi pengguna

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi Room database
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "app_db")
            .fallbackToDestructiveMigration()
            .build()

        // Inisialisasi PrefManager
        prefManager = PrefManager(this)

        // Tombol navigasi ke MovieCollection
        binding.btnMasuk.setOnClickListener {
            startActivity(Intent(this, MovieCollection::class.java))
        }

        // Bisa menambahkan logika atau tampilan lain sesuai dengan role pengguna atau data yang tersimpan
        // Contoh:
        val username = prefManager.getLoggedInUsername()  // Pastikan ini ada di PrefManager
        binding.tvWelcome.text = "Halo, $username"
    }
}
