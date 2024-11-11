package com.example.dataemail

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.HideReturnsTransformationMethod
import android.text.method.LinkMovementMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.dataemail.admin.DashboardActivity
import com.example.dataemail.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var db: AppDatabase
    private lateinit var preferenceHelper: PrefManager
    private var isPasswordVisible: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Room database and PrefManager
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "app_db")
            .fallbackToDestructiveMigration()
            .build()
        preferenceHelper = PrefManager(this)

        // Setup Spinner with roles (Admin or User)
        val roles = listOf("User", "Admin")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, roles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerRoleLogin.adapter = adapter

        // Login button click listener
        binding.btnLogin.setOnClickListener {
            loginUser()
        }

        setupRegisterText()
    }

    private fun loginUser() {
        val username = binding.lusername.text.toString()
        val password = binding.lpassword.text.toString()

        if (username.isBlank() || password.isBlank()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            // Check if user exists with matching username and password
            val user = db.userDao().login(username, password)
            if (user != null) {
                // Save user data in PrefManager or SharedPreferences
                preferenceHelper.saveUser(
                    user.username,
                    user.email,
                    user.phone,
                    user.password,
                    user.profilePhotoUrl ?: "",
                    user.role
                )

                // Get the selected role from the spinner
                val selectedRole = binding.spinnerRoleLogin.selectedItem.toString()

                // Navigate to the corresponding activity based on role
                val intent = if (selectedRole == "Admin") {
                    Intent(this@LoginActivity, DashboardActivity::class.java)
                } else {
                    Intent(this@LoginActivity, HomeActivity::class.java)
                }

                startActivity(intent)
                finish() // Close LoginActivity
            } else {
                Toast.makeText(this@LoginActivity, "Login failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun togglePasswordVisibility() {
        if (isPasswordVisible) {
            binding.lpassword.transformationMethod = PasswordTransformationMethod.getInstance()
            binding.lpassword.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.ic_lock, 0, R.drawable.ic_eye_close, 0
            )
        } else {
            binding.lpassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            binding.lpassword.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.ic_lock, 0, R.drawable.ic_eye_open, 0
            )
        }

        binding.lpassword.setSelection(binding.lpassword.text.length)
        isPasswordVisible = !isPasswordVisible
    }

    private fun setupRegisterText() {
        val text = "New Member? Register"
        val spannableString = SpannableString(text)

        val registerSpan = object : ClickableSpan() {
            override fun onClick(widget: android.view.View) {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        }

        spannableString.setSpan(
            ForegroundColorSpan(Color.BLUE),
            text.indexOf("Register"),
            text.indexOf("Register") + "Register".length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannableString.setSpan(
            registerSpan,
            text.indexOf("Register"),
            text.indexOf("Register") + "Register".length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        with(binding) {
            tvRegister.text = spannableString
            tvRegister.movementMethod = LinkMovementMethod.getInstance()
        }
    }
}

