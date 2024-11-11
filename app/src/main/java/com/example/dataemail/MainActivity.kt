package com.example.dataemail

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dataemail.admin.DashboardActivity
import com.example.dataemail.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize SharedPreferences
        sharedPref = getSharedPreferences("user_pref", MODE_PRIVATE)

        // Tombol "Get Started" yang mengarah ke halaman Login
        binding.btnMasuk.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
        }

        // Cek jika pengguna sudah login
        val isLoggedIn = sharedPref.contains("username")
        if (isLoggedIn) {
            // Jika sudah login, arahkan ke halaman utama (misalnya HomeActivity)
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish() // Menghindari kembali ke MainActivity setelah login
        }

        // Tombol "Masuk" yang mengarah ke halaman Login jika tidak login
        binding.btnMasuk.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
